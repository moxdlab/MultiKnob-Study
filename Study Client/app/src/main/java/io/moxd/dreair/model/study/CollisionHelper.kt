package io.moxd.dreair.model.study

import io.moxd.dreair.model.collision.CollisionDetector
import io.moxd.dreair.model.task.Task
import io.moxd.dreair.utils.CoroutineSingleTimer
import io.moxd.dreair.utils.CoroutineSingleTimer.trySchedule
import kotlinx.coroutines.CoroutineScope

class CollisionHelper(
    private val collisionDetector: CollisionDetector,
    private val task: Task,
    private val onDwell: suspend () -> Unit
) {

    fun handleCollision(scope: CoroutineScope, cursorPos: Float) {
        if (collisionDetector.collides(cursorPos, task.goalPos)) {
            scope.trySchedule(1000) {
                onDwell()
            }
        } else {
            CoroutineSingleTimer.tryCancel()
        }
    }
}