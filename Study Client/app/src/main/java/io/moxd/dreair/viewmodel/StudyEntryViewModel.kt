package io.moxd.dreair.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.moxd.dreair.model.controller.SpeedPermutations.getPermutationsForSubjectId
import io.moxd.dreair.model.persistence.AppDatabase
import io.moxd.dreair.ui.compose.StudyEntryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val NUMBER_OF_SESSIONS_PER_USER = 3
class StudyEntryViewModel(app: Application) : AndroidViewModel(app) {

    private val sessionDao = AppDatabase.getInstance(app.applicationContext).sessionDao()

    private val _currentSubject: MutableLiveData<Int> = MutableLiveData()
    val currentSubjectId: LiveData<Int> = _currentSubject

    private val _completedSessions: MutableLiveData<Int> = MutableLiveData()
    val completedSessions: LiveData<Int> = _completedSessions

    private val _studyEntryData: MutableLiveData<StudyEntryData> = MutableLiveData()
    val studyEntryData: LiveData<StudyEntryData> = _studyEntryData

    fun fetchAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val subjectDeferred = async { sessionDao.getLatestSession()?.subjectId ?: 1 }
            var subjectId = subjectDeferred.await()
            if (subjectId < 0) subjectId = 1 //first user aborted

            val completedSessions = sessionDao.getSessionCountForSubject(subjectId)
            if (completedSessions == NUMBER_OF_SESSIONS_PER_USER) {
                postFreshValues(subjectId + 1)
            } else {
                postValues(subjectId, completedSessions)
            }
        }
    }

    private fun postFreshValues(nextSubjectId: Int) {
        postValues(nextSubjectId, 0)
    }

    private fun postValues(subjectId: Int, completedSessions: Int) {
        _currentSubject.postValue(subjectId)
        _completedSessions.postValue(completedSessions)

        val permutation = getPermutationsForSubjectId(subjectId)
        val data = StudyEntryData(
            permutation.first.name,
            permutation.second.name,
            permutation.third.name,
            completedSessions > 0,
            completedSessions > 1,
            false
        )
        _studyEntryData.postValue(data)
    }
}