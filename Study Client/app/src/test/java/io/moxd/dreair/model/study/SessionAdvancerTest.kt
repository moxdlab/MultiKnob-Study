package io.moxd.dreair.model.study

import io.moxd.dreair.model.controller.ControllerSpeed
import io.moxd.dreair.model.persistence.Session
import io.moxd.dreair.model.task.TaskHelper
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SessionAdvancerTest {

    private val taskHelper = mock<TaskHelper>()
    private val sessionAdvancer = SessionAdvancer(taskHelper)

    @Test
    fun `should advance to first ever session`() {
        val noExistingSessions = null
        assertTrue(sessionAdvancer.shouldAdvance(noExistingSessions))
    }

    @Test
    fun `should advance on completed session`() {
        val completedSession = fakeSession.copy(completionTimestamp = 1L)
        assertTrue(sessionAdvancer.shouldAdvance(completedSession))
    }

    @Test
    fun `should advance any session on completed tasks`() {
        whenever(taskHelper.isEndReached()).thenReturn(true)
        assertTrue(sessionAdvancer.shouldAdvance(fakeSession))
    }

}

val fakeSession = Session(subjectId = 12, controllerSpeed = ControllerSpeed.Fast)