package io.moxd.dreair.model.study

import io.moxd.dreair.model.persistence.Session

object SubjectHelper {

    fun getSubjectIdForUpcomingSession(previousSession: Session?): Int {
        return when {
            previousSession == null -> 1 //first time use, new subject
            previousSession.id % 3 == 0L -> previousSession.subjectId.inc() //next subject
            else -> previousSession.subjectId //in the middle of the study, same subject
        }
    }
}