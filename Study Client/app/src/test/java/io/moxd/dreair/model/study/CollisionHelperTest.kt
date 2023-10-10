package io.moxd.dreair.model.study

import io.moxd.dreair.model.collision.CollisionDetector
import io.moxd.dreair.model.task.Task
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class CollisionHelperTest {

    private val collision = 1f
    private val noCollision = 0f
    private val collidingDetectorMock = mock<CollisionDetector> {
        on { collides(collision, 1f) }.thenReturn(true)
        on { collides(noCollision, 1f) }.thenReturn(false)
    }
    private val anyTask = Task.new(0.5f, 1f)

    @Test
    fun `should not execute dwell if not dwelling for 1 second or more`() = runTest {
        val collisionHelper = CollisionHelper(collidingDetectorMock, anyTask) {
            fail("shouldn't reach dwell :(")
        }
        collisionHelper.handleCollision(this, collision)
        delay(500)
        collisionHelper.handleCollision(this, noCollision)
    }

    @Test
    fun `should execute dwell if dwelling for 1 second or more`() = runTest {
        var failed = true
        val collisionHelper = CollisionHelper(collidingDetectorMock, anyTask) {
            failed = false
        }
        collisionHelper.handleCollision(this, collision)
        delay(1001)
        collisionHelper.handleCollision(this, noCollision)
        if (failed) fail("should have but didn't called dwell")
    }
}