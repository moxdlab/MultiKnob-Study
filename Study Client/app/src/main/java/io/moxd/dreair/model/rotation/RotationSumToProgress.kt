package io.moxd.dreair.model.rotation

import io.moxd.dreair.model.controller.ControllerState
import kotlin.math.abs

class RotationSumToProgress(
    controllerTicks: Int = 72, // 5°
    private val progressPerDegree: Float = 0.01f,
    private val fingerScale: (fingerCount: Int) -> Float,
) {

    private var lastRotationSum: Float? = null
    private var degreesPerTick = 360f / controllerTicks
    var isInitialized = false
        private set

    fun init(initialState: ControllerState) {
        lastRotationSum = initialState.rotationSum
        isInitialized = true
    }

    fun getProgressFor(newState: ControllerState): Float {
        val last = abs(lastRotationSum!!)
        val new = abs(newState.rotationSum)

        val diff = new - last
        if (abs(diff) < degreesPerTick) {
            return 0f
        }

        val progress = diff * progressPerDegree * fingerScale(newState.fingercount)
        lastRotationSum = newState.rotationSum
        return progress
    }
}