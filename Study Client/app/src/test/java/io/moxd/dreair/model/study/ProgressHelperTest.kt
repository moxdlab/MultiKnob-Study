package io.moxd.dreair.model.study

import io.moxd.dreair.model.controller.ControllerState
import io.moxd.dreair.model.task.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressHelperTest {

    private val initialProgress = 0.5f
    private val fakeTask = Task.new(initialProgress, 1f)
    private val progressHelper = ProgressHelper(fakeSession, fakeTask)

    @Test
    fun `should not update progress if no change`() {
        val noRotationState = ControllerState(3, 0f, 100f)
        val actualProgress = progressHelper.updateProgress(noRotationState)
        assertEquals(initialProgress, actualProgress)
    }

    @Test
    fun `positive degree should update to negative progress`() {
        val rotationState = ControllerState(5, 5f, 100f)
        val actualProgress = progressHelper.updateProgress(rotationState)
        assertTrue(actualProgress < initialProgress)
    }

    //TODO: more tests? like higher Speed -> greater Progress, or exact progress test?
}