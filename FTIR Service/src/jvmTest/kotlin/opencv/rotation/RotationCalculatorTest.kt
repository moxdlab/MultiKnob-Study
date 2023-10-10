package opencv.rotation

import org.junit.Assert.*
import org.junit.Test

class RotationCalculatorTest {

    @Test
    fun `should apply rotation correctly`() {
        val rotationCalc = RotationCalculator(0.0, 0.0)
        rotationCalc.initRotation(1.0, 0.0)
        val delta = rotationCalc.applyRotation(0.0, 1.0)
        assertEquals(90.0, delta, 0.001)
    }
}