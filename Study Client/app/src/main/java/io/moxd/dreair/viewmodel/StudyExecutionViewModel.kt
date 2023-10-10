package io.moxd.dreair.viewmodel

import android.app.Application
import androidx.lifecycle.*
import io.moxd.dreair.model.study.StudyCallbacks
import io.moxd.dreair.model.study.StudyHelper
import io.moxd.dreair.model.collision.CollisionDetector
import io.moxd.dreair.model.collision.Enter
import io.moxd.dreair.model.persistence.AppDatabase
import io.moxd.dreair.model.persistence.SessionPersistence
import io.moxd.dreair.model.study.MqttHelper
import io.moxd.dreair.model.task.Task
import io.moxd.dreair.model.task.TaskHelper
import io.moxd.dreair.ui.compose.Metadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class StudyExecutionViewModel(app: Application) : AndroidViewModel(app) {

    private val db: AppDatabase by lazy { AppDatabase.getInstance(app.applicationContext) }
    private val collisionDetector = CollisionDetector(Enter)
    var laneWidth: Int by Delegates.observable(0) { _, _, new ->
        collisionDetector.viewWidth = new
    }
    var goalWidth: Int by Delegates.observable(0) { _, _, new ->
        collisionDetector.goalWidth = new
    }
    var cursorWidth: Int by Delegates.observable(0) { _, _, new ->
        collisionDetector.cursorWidth = new
    }

    private val _goalPos: MutableLiveData<Float> = MutableLiveData()
    val goalPos: LiveData<Float> = _goalPos

    private val _cursorPos: MutableLiveData<Float> = MutableLiveData()
    val cursorPos: LiveData<Float> = _cursorPos


    private val _metadata: MutableLiveData<Metadata?> = MutableLiveData()
    val metadata: LiveData<Metadata?> = _metadata

    var navigateToCountdown: () -> Unit = { }
    var navigateToEntry: () -> Unit = { }

    private val studyHelper = StudyHelper(
        collisionDetector = collisionDetector,
        sessionPersistence = SessionPersistence(db.sessionDao()),
        mqttHelper = MqttHelper(db.mqttDataDao()),
        taskProgressDao = db.taskProgressDao(),
        studyCallbacks = object : StudyCallbacks {
            override suspend fun onNewTask(task: Task) {
                _goalPos.postValue(task.goalPos)
                _cursorPos.postValue(task.initialCursorPos)
                updateMetadata()
            }

            override fun onNewCursorPos(cursorPos: Float) {
                _cursorPos.postValue(cursorPos)
            }

            override suspend fun onTaskEnd() {
                withContext(Dispatchers.Main) {
                    navigateToCountdown()
                }
            }

            override suspend fun onSessionEnd() {
                withContext(Dispatchers.Main) {
                    navigateToEntry()
                }
            }
        }
    )

    fun nextTask() {
        viewModelScope.launch(Dispatchers.IO) {
            studyHelper.run()
            updateMetadata()
        }
    }

    fun abort() {
        viewModelScope.launch(Dispatchers.IO) {
            studyHelper.wipeSessions()
        }
    }

    private suspend fun updateMetadata() {
        db.sessionDao().getLatestSession()?.let {
            _metadata.postValue(
                Metadata(
                    it.subjectId,
                    it.id,
                    TaskHelper.currentId,
                    it.controllerSpeed.name
                )
            )
        }
    }
}