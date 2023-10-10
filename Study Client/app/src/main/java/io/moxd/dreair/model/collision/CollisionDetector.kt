package io.moxd.dreair.model.collision

import kotlin.properties.Delegates

class CollisionDetector(private val strategy: CollisionStrategy) {

    var cursorWidth by Delegates.notNull<Int>()
    var goalWidth by Delegates.notNull<Int>()
    var viewWidth by Delegates.notNull<Int>()

    fun collides(cursorPos: Float, goalPos: Float): Boolean {
        val cursorOffset = getCursorOffset(cursorPos)
        val goalOffset = getGoalOffset(goalPos)
        return strategy.collides(cursorWidth, cursorOffset, goalWidth, goalOffset)
    }

    private fun getCursorOffset(cursorPos: Float): Float {
        return getOffset(cursorWidth, cursorPos)
    }

    private fun getGoalOffset(goalPos: Float): Float {
        return getOffset(goalWidth, goalPos)
    }

    private fun getOffset(elementWidth: Int, elementPos: Float): Float {
        return when (elementPos) {
            0f -> 0f
            1f -> (viewWidth - elementWidth).toFloat()
            else -> viewWidth * elementPos - (elementWidth / 2)
        }
    }
}

object Enter : CollisionStrategy {
    override fun collides(
        cursorWidth: Int,
        cursorOffset: Float,
        goalWidth: Int,
        goalOffset: Float
    ): Boolean {
        return cursorOffset >= goalOffset && cursorOffset + cursorWidth <= goalOffset + goalWidth
    }
}

interface CollisionStrategy {
    fun collides(cursorWidth: Int, cursorOffset: Float, goalWidth: Int, goalOffset: Float): Boolean
}