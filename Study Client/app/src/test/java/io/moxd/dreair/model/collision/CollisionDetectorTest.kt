package io.moxd.dreair.model.collision

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class CollisionDetectorTest() {

/*
    assuming a view width of 1000px:
     - goalbox is 200px large, positioned at 0.0 or 1.0
     - the goalbox *should* spread around 0-200 (0.0) or 800-1000 (1.0)
     - this might seem counter intuitive, as the goalbox centers around the goalpos in most cases
     - but if it would, a center around 0.0 or 1.0 would shrink the views to half their size
     - a goalbox 900px large would look the same at pos 0.6-1.0
        - however we only check the borders, not 900px + 0.6 cases
        - why? because this logic is first and foremost for the cursor (small!), not the goal
          to keep it within bounds
*/

    private val strategyMock = mock<CollisionStrategy>()
    private val detector = CollisionDetector(strategyMock).also {
        it.viewWidth = 1000
        it.goalWidth = 200
        it.cursorWidth = 10
    }

    @Test
    fun `should left align view at pos 0`() {
        detector.collides(0.0f, 0.0f)
        val expectedOffset = 0f
        verify(strategyMock).collides(10, expectedOffset, 200, expectedOffset)
    }

    @Test
    fun `should right align view at pos 1`() {
        detector.collides(1.0f, 1.0f)
        val expectedCursorOffset = 990f
        val expectedGoalboxOffset = 800f
        verify(strategyMock).collides(10, expectedCursorOffset, 200, expectedGoalboxOffset)
    }


    @Test
    fun `should center align view at pos gt 0 and lt 1`() {
        detector.collides(0.5f, 0.5f)
        val expectedCursorOffset = 495f
        val expectedGoalboxOffset = 400f
        verify(strategyMock).collides(10, expectedCursorOffset, 200, expectedGoalboxOffset)
    }
}