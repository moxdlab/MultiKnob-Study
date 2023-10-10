package io.moxd.dreair.model.controller

sealed class ControllerSpeed {
    abstract val fingerScale: (fingerCount: Int) -> Float
    abstract val char: Char
    abstract val name: String

    companion object {
        fun get(char: Char): ControllerSpeed? = when (char) {
            Fast.char -> Fast
            Slow.char -> Slow
            Mixed.char -> Mixed
            else -> null
        }
    }

    object Slow : ControllerSpeed() {
        override val fingerScale: (fingerCount: Int) -> Float = { 0.1f }
        override val char = 'S'
        override val name = "Slow"
    }

    object Fast : ControllerSpeed() {
        override val fingerScale: (fingerCount: Int) -> Float = { 2f }
        override val char = 'F'
        override val name = "Fast"
    }

    object Mixed : ControllerSpeed() {
        override val fingerScale: (fingerCount: Int) -> Float = { fingerCount ->
            when (fingerCount) {
                1, 2, 3 -> 0.1f
                4, 5, 6 -> 2f
                else -> 0f
            }
        }
        override val char = 'M'
        override val name = "Mixed"
    }
}

object SpeedPermutations {

    private val permutations: List<Triple<ControllerSpeed, ControllerSpeed, ControllerSpeed>> =
        listOf(
            Triple(ControllerSpeed.Slow, ControllerSpeed.Fast, ControllerSpeed.Mixed),
            Triple(ControllerSpeed.Slow, ControllerSpeed.Mixed, ControllerSpeed.Fast),
            Triple(ControllerSpeed.Fast, ControllerSpeed.Slow, ControllerSpeed.Mixed),
            Triple(ControllerSpeed.Fast, ControllerSpeed.Mixed, ControllerSpeed.Slow),
            Triple(ControllerSpeed.Mixed, ControllerSpeed.Slow, ControllerSpeed.Fast),
            Triple(ControllerSpeed.Mixed, ControllerSpeed.Fast, ControllerSpeed.Slow),
        )

    fun getPermutationsForSubjectId(subjectId: Int) =
        permutations[(subjectId - 1) % permutations.size]
}
