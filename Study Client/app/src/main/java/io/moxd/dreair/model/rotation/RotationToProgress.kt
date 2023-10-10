package io.moxd.dreair.model.rotation

import io.moxd.dreair.model.controller.ControllerState

fun rotationToProgress(
    newState: ControllerState,
    progressPerDegree: Float = 0.01f,
    fingerScale: (fingerCount: Int) -> Float
): Float {
    val scale = fingerScale(newState.fingercount)
    return newState.rotation * progressPerDegree * scale
}