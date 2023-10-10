package io.moxd.dreair.model.study

import io.moxd.dreair.model.controller.ControllerSpeed
import io.moxd.dreair.model.controller.SpeedPermutations
import io.moxd.dreair.model.persistence.Session

class ControllerSpeedHelper(
    private val subjectHelper: SubjectHelper = SubjectHelper,
    private val speedPermutations: SpeedPermutations = SpeedPermutations
) {

    fun getControllerSpeedForUpcomingSession(previousSession: Session?): ControllerSpeed {
        val upcomingSubjectId = subjectHelper.getSubjectIdForUpcomingSession(previousSession)
        val controllerSpeeds = speedPermutations.getPermutationsForSubjectId(upcomingSubjectId)

        return when (previousSession?.id?.rem(3L)) {
            0L -> controllerSpeeds.first //new subject
            1L -> controllerSpeeds.second //same subject
            2L -> controllerSpeeds.third //same subject
            else -> controllerSpeeds.first //first session ever

        }
    }
}