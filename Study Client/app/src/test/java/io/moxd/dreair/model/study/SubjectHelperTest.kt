package io.moxd.dreair.model.study

import io.moxd.dreair.model.controller.ControllerSpeed
import io.moxd.dreair.model.persistence.Session
import org.junit.Assert.*
import org.junit.Test

class SubjectHelperTest {

    @Test
    fun `first subject should get id 1`() {
        val noExistingPrevSession: Session? = null
        val actualSubjectId = SubjectHelper.getSubjectIdForUpcomingSession(
            previousSession = noExistingPrevSession
        )
        val expectedSubjectId = 1
        assertEquals(expectedSubjectId, actualSubjectId)
    }

    @Test
    fun `after three sessions the subject id must be 2`() {
        val firstSubjectFinished =
            Session(id = 3, subjectId = 1, controllerSpeed = ControllerSpeed.Mixed)

        val actualSubjectId = SubjectHelper.getSubjectIdForUpcomingSession(
            previousSession = firstSubjectFinished
        )
        val expectedSubjectId = 2
        assertEquals(expectedSubjectId, actualSubjectId)
    }

    @Test
    fun `subject id should not change if user hasn't finished 3 sessions`() {
        val firstSessionFinished =
            (Session(id = 2, subjectId = 1, controllerSpeed = ControllerSpeed.Slow))

        val actualSubjectId = SubjectHelper.getSubjectIdForUpcomingSession(
            previousSession = firstSessionFinished
        )
        val expectedSubjectId = 1
        assertEquals(expectedSubjectId, actualSubjectId)
    }
}