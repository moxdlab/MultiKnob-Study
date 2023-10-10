package io.moxd.dreair.model.study

import io.moxd.dreair.model.persistence.Session
import io.moxd.dreair.model.task.TaskHelper

class SessionAdvancer(private val taskHelper: TaskHelper = TaskHelper) {

    fun shouldAdvance(latestSession: Session?): Boolean {
        return when {
            isFirstEverSession(latestSession) -> true
            sessionDoneAlready(latestSession) -> true
            allTasksCompleted() -> true
            else -> false
        }
    }

    private fun isFirstEverSession(session: Session?) = session == null
    private fun allTasksCompleted() = taskHelper.isEndReached()
    private fun sessionDoneAlready(session: Session?): Boolean {
        return session?.completionTimestamp != null
    }
}
