package io.moxd.dreair.model.collision

import io.moxd.dreair.utils.toCurry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EnterTest {

/*
    assuming a view width of 1000px:
     - goalbox spans from 0.6 to 0.8 (as it is 200px large), and centers at 0.7
     - thus, the cursor offset must be >= 600 and <= 790
     - offset = start of widget, independent of its width
 */

    private val collidesWithPreset: ((andCursorOffset: Float) -> Boolean) = Enter.let {
        val cursorWidth = 10
        val goalWidth = 200
        val goalOffset = 600f
        it::collides.toCurry()(cursorWidth)(goalWidth)(goalOffset)
    }

    @Test
    fun `should return no collision as cursor is way outside goal`() {
        assertFalse(collidesWithPreset(295f))
    }

    @Test
    fun `should return no collision as cursor is touching but still not completely inside goal`() {
        assertFalse(collidesWithPreset(595f))
    }

    @Test
    fun `should return a collision as cursor is barely completely inside goal`() {
        assertTrue(collidesWithPreset(600f))
    }

    @Test
    fun `should return a collision as cursor is somewhere inside goal`() {
        assertTrue(collidesWithPreset(771f))
    }

    @Test
    fun `should return a collision as cursor is still but barely completely inside goal`() {
        assertTrue(collidesWithPreset(790f))
    }

    @Test
    fun `should return no collision as cursor is touching but already outside goal`() {
        assertFalse(collidesWithPreset(791f))
    }

}