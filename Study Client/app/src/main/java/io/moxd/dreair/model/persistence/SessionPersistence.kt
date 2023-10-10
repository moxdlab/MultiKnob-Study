package io.moxd.dreair.model.persistence

import io.moxd.dreair.model.controller.ControllerSpeed
import io.moxd.dreair.model.study.ControllerSpeedHelper
import io.moxd.dreair.model.study.SubjectHelper
import kotlin.random.Random
import kotlin.random.nextInt

class SessionPersistence(private val sessionDao: SessionDao) {

    suspend fun currentSession(): Session? {
        return sessionDao.getLatestSession()
    }

    // called on first app start, or on session change
    suspend fun newSession(): Session {
        val previousSession = sessionDao.getLatestSession()
        val nextSubjectId = getSubjectId(previousSession)
        val nexControllerSpeed = getControllerSpeed(previousSession)
        return createNewSession(nextSubjectId, nexControllerSpeed)
    }

    //when all tasks for a session is completed, add completion timestamp
    suspend fun completeLatestSession() {
        sessionDao.getLatestSession()?.let {
            sessionDao.completeSession(it.id)
        }
    }

    /**
     * Doesn't really wipe sessions, but changes the subjectId to a random negative int.
     * This way the next subject-id and session is exactly the same.
     * Note: can create the same random value twice. Results in not being able to associate a
     * wiped session to a user (rare + wiped sessions shouldn't be needed anyways).
     */
    suspend fun wipeLatestSessions() {
        val latestSession = sessionDao.getLatestSession() ?: return
        sessionDao.updateSubjectId(
            latestSession.subjectId,
            Random.nextInt(-Int.MAX_VALUE..-1)
        )
    }

    suspend fun isSubjectDone(subjectId: Int): Boolean {
        return sessionDao.getSessionCompletedCount(subjectId) == 3
    }

    private fun getSubjectId(previousSession: Session?) =
        SubjectHelper.getSubjectIdForUpcomingSession(previousSession)

    private fun getControllerSpeed(previousSession: Session?) =
        ControllerSpeedHelper().getControllerSpeedForUpcomingSession(previousSession)

    private suspend fun createNewSession(
        subjectId: Int,
        controllerSpeed: ControllerSpeed
    ): Session {

        val id = sessionDao.insert(
            Session(subjectId = subjectId, controllerSpeed = controllerSpeed)
        )
        return sessionDao.findById(id)
    }
}