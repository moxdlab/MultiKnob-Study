package io.moxd.dreair.model.study

import io.moxd.dreair.model.StatefulProgress
import io.moxd.dreair.model.controller.ControllerState
import io.moxd.dreair.model.persistence.Session
import io.moxd.dreair.model.task.Task

const val PROGRESS_PER_DEGREE = 0.01f

class ProgressHelper(
    private val session: Session,
    task: Task,
) {

    private val totalProgress = StatefulProgress(task.initialCursorPos)

    fun updateProgress(newState: ControllerState): Float {
        val fingerScale = session.controllerSpeed.fingerScale
        val scale = fingerScale(newState.fingercount)
        val progress = newState.rotation * PROGRESS_PER_DEGREE * scale
        return totalProgress.apply(-progress)
    }
}