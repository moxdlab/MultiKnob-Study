package opencv.rotation

import opencv.internal.Circle

/**
 * NOT THREAD SAFE!
 */
class RotationHelper {

    private lateinit var center: Circle
    private lateinit var rotationCalculator: RotationCalculator
    private lateinit var lastCircle: Circle
    private var initialized = false
    private var rotationSum = 0.0

    fun provideCenter(center: Circle) {
        this.center = center
        rotationCalculator = RotationCalculator(center.x, center.y)
    }

    fun feedCircle(rotationCircle: Circle?): RotationResult? {
        if(rotationCircle == null) return null
        if (!initialized) {
            initialize(rotationCircle)
            return null
        }
        val rotation = rotationCalculator.applyRotation(rotationCircle.x, rotationCircle.y)
        rotationSum += rotation
        lastCircle = rotationCircle
        return RotationResult(rotation, rotationSum)
    }

    private fun initialize(circle: Circle) {
        lastCircle = circle
        rotationCalculator.initRotation(lastCircle.x, lastCircle.y)
        initialized = true
    }
}

data class RotationResult(val rotation: Double, val rotationSum: Double)