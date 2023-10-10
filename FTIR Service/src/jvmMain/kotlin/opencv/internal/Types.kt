package opencv.internal

import org.opencv.core.Mat
import org.opencv.core.Point

data class Circle(val x: Double, val y: Double, val radius: Double) {
    fun center() = Point(x, y)
}

class BlurredMat : Mat()
class GreyscaledMat : Mat()
class Mask : Mat()