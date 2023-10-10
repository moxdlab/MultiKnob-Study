package opencv.rotation

import kotlin.math.atan2

class RotationCalculator(private val centerX: Double, private val centerY: Double) {

    private var lastAngle = 0.0

    fun initRotation(x: Double, y: Double) {
        lastAngle = computeAngle(x, y)
    }

    fun applyRotation(x: Double, y: Double): Double {
        val newAngle = computeAngle(x, y)
        val delta = getDelta(newAngle, lastAngle)
        lastAngle = newAngle
        return delta
    }

    private fun computeAngle(x: Double, y: Double): Double {
        val radians = atan2(x - centerX, centerY - y)
        val degrees = Math.toDegrees(radians)
        return degreesTo360(degrees)
    }
}

private fun getDelta(newAngle: Double, oldAngle: Double): Double {
    val diff = newAngle - oldAngle
    return when {
        diff > 180 -> -(360 - diff)
        diff < -180 -> 360 + diff
        else -> diff
    }
}

private fun degreesTo360(degrees: Double) = if (degrees >= 0) degrees else 360 + degrees
