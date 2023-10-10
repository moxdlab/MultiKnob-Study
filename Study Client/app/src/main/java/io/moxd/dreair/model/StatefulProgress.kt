package io.moxd.dreair.model

class StatefulProgress(initialProgress: Float) {

    private var currentProgress = initialProgress

    fun apply(progress: Float): Float {
        currentProgress += progress

        currentProgress = when {
            currentProgress < 0f -> 0f
            currentProgress > 1f -> 1f
            else -> currentProgress
        }
        return currentProgress
    }

    fun reset(initialProgress: Float) {
        currentProgress = initialProgress
    }
}