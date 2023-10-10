package io.moxd.dreair.viewmodel

import androidx.lifecycle.*
import io.moxd.dreair.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val COUNTDOWN_DURATION = if (BuildConfig.BUILD_TYPE == "debug") 0 else 5

class CountdownViewModel : ViewModel() {

    private val _timeLeft: MutableLiveData<Int> = MutableLiveData()
    val timeLeft: LiveData<Int>
        get() = _timeLeft

    var navigateToExecution: () -> Unit = {}

    private val countdownFlow = (COUNTDOWN_DURATION downTo 0).asFlow().onEach {
        if (it != COUNTDOWN_DURATION) delay(1000)
        if (it == 0) withContext(Dispatchers.Main) { navigateToExecution() }
    }

    fun startCountdown() {
        viewModelScope.launch {
            countdownFlow.collect {
                _timeLeft.postValue(it)
            }
        }
    }

}