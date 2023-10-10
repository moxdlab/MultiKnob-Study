package io.moxd.dreair.model.study

import io.moxd.dreair.model.collision.CollisionDetector
import io.moxd.dreair.model.persistence.*
import io.moxd.dreair.model.task.Task
import io.moxd.dreair.model.task.TaskHelper
import kotlinx.coroutines.*

class StudyHelper(
    private val studyCallbacks: StudyCallbacks,
    private val collisionDetector: CollisionDetector,
    private val sessionPersistence: SessionPersistence,
    private val mqttHelper: MqttHelper,
    private val taskProgressDao: TaskProgressDao,
    private val taskHelper: TaskHelper = TaskHelper,
    private val sessionAdvancer: SessionAdvancer = SessionAdvancer()
) {


    suspend fun run() {
        //TODO: check if recovery is needed
        val latestSession = sessionPersistence.currentSession()
        val session = if (sessionAdvancer.shouldAdvance(latestSession)) {
            sessionPersistence.newSession()
        } else {
            latestSession!!
        }
        nextTask(session)
    }

    suspend fun wipeSessions() {
        mqttHelper.stopMqtt("subject aborted")
        sessionPersistence.wipeLatestSessions()
        taskHelper.reset()
        studyCallbacks.onSessionEnd()
    }

    private suspend fun nextTask(session: Session) {
        val next = taskHelper.getNext()
        studyCallbacks.onNewTask(next)
        startMqtt(session, next)
    }

    private suspend fun startMqtt(session: Session, task: Task) {
        val progressHelper = ProgressHelper(session, task)
        val collisionHelper = CollisionHelper(collisionDetector, task) {
            mqttHelper.stopMqtt("task finished")
            taskFinished()
        }

        coroutineScope {
            val outerScope = this
            mqttHelper.startMqtt { mqttData, controllerState ->
                val cursorPos = progressHelper.updateProgress(controllerState)
                outerScope.persistProgress(session, task, cursorPos, mqttData)
                studyCallbacks.onNewCursorPos(cursorPos)
                collisionHelper.handleCollision(outerScope, cursorPos)
            }
        }
    }

    private suspend fun taskFinished() {
        if (taskHelper.isEndReached()) {
            sessionPersistence.completeLatestSession()
            studyCallbacks.onSessionEnd()
        } else {
            studyCallbacks.onTaskEnd()
        }
    }

    private fun CoroutineScope.persistProgress(
        session: Session,
        task: Task,
        cursorPos: Float,
        mqttData: MqttData?
    ) {
        launch(Dispatchers.IO) {
            val taskProgress = TaskProgress(
                currentPos = cursorPos, taskId = task.id,
                sessionIdFk = session.id, mqttDataIdFk = mqttData?.pythonTimestamp ?: -1L
            )
            taskProgressDao.insert(taskProgress)
        }
    }
}

interface StudyCallbacks {
    suspend fun onNewTask(task: Task)
    fun onNewCursorPos(cursorPos: Float)
    suspend fun onTaskEnd()
    suspend fun onSessionEnd()
}