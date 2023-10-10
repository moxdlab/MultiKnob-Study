package io.moxd.dreair.utils

import kotlinx.coroutines.*

/**
This Timer does only process one action at at time. Scheduling more than one action results in
ignoring all following actions but the first one.
 */
object CoroutineSingleTimer {

    private var job: Job? = null

    /**
     * schedules work only if no pending task is present
     */
    fun CoroutineScope.trySchedule(delay: Long, action: suspend () -> Unit) {
        if (job != null) return
        job = launch {
            delay(delay)
            action()
        }
    }

    fun tryCancel() {
        job?.cancel()
        job = null
    }
}