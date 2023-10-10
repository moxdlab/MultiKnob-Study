package io.moxd.dreair.model.study

import io.moxd.dreair.model.controller.ControllerSpeed
import io.moxd.dreair.model.controller.SpeedPermutations
import io.moxd.dreair.model.persistence.Session
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class ControllerSpeedHelperTest {

    private val fakeSpeeds =
        Triple(ControllerSpeed.Slow, ControllerSpeed.Fast, ControllerSpeed.Mixed)

    private val permutationMock = Mockito.mock(SpeedPermutations::class.java).also {
        Mockito.`when`(it.getPermutationsForSubjectId(Mockito.anyInt()))
            .thenReturn(fakeSpeeds)
    }
    private val sut = ControllerSpeedHelper(speedPermutations = permutationMock)

    @Test
    fun `first ever session should use first controller speed`() {
        val noExistingPrevSession: Session? = null
        val actualSpeed = sut.getControllerSpeedForUpcomingSession(noExistingPrevSession)
        assertEquals(
            "expected to get the first value of triple, but got another one",
            fakeSpeeds.first,
            actualSpeed
        )
    }

    @Test
    fun `first session for non first user should use first controller speed`() {
        val firstSubjectFinished =
            Session(id = 3, subjectId = 1, controllerSpeed = fakeSpeeds.third)

        val actualSpeed = sut.getControllerSpeedForUpcomingSession(firstSubjectFinished)
        assertEquals(
            "expected to get the first value of triple, but got another one",
            fakeSpeeds.first,
            actualSpeed
        )
    }

    @Test
    fun `second session for user should use second controller speed`() {
        val user10Session1 =
            Session(id = 31, subjectId = 10, controllerSpeed = fakeSpeeds.first)

        val actualSpeed = sut.getControllerSpeedForUpcomingSession(user10Session1)
        assertEquals(
            "expected to get the second value of triple, but got another one",
            fakeSpeeds.second,
            actualSpeed
        )
    }

    @Test
    fun `third session for user should use third controller speed`() {
        val user2Session2 =
            Session(id = 5, subjectId = 2, controllerSpeed = fakeSpeeds.second)

        val actualSpeed = sut.getControllerSpeedForUpcomingSession(user2Session2)
        assertEquals(
            "expected to get the third value of triple, but got another one",
            fakeSpeeds.third,
            actualSpeed
        )
    }
}