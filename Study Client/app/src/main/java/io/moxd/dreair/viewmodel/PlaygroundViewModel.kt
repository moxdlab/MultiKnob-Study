package io.moxd.dreair.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.moxd.dreair.model.*
import io.moxd.dreair.model.controller.ControllerSpeed
import io.moxd.dreair.model.controller.ControllerState
import io.moxd.dreair.model.rotation.rotationToProgress
import io.moxd.dreair.model.study.MqttHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaygroundViewModel : ViewModel() {

    val initialCursorPos = 0.3f
    private var controllerSpeed: ControllerSpeed = ControllerSpeed.Slow

    private val _fingerCount: MutableLiveData<Int> = MutableLiveData()
    val fingerCount: LiveData<Int> = _fingerCount

    private val _cursorPos: MutableLiveData<Float> = MutableLiveData(initialCursorPos)
    val cursorPos: LiveData<Float> = _cursorPos

    private val mqttHelper = MqttHelper()

    fun startPlayground() {
        _cursorPos.postValue(initialCursorPos)
        viewModelScope.launch(Dispatchers.IO) {
            mqttListen()
        }
    }

    private suspend fun mqttListen() {

        val totalProgress = StatefulProgress(initialCursorPos)
        fun progressToPos(controllerState: ControllerState): Float {
            val progress =
                rotationToProgress(controllerState, fingerScale = controllerSpeed.fingerScale)
            return totalProgress.apply(-progress)
        }

        mqttHelper.startMqtt { _, cs ->
            val cursorPos = progressToPos(cs)
            _cursorPos.postValue(cursorPos)
            _fingerCount.postValue(cs.fingercount)
        }
    }

    fun close() {
        mqttHelper.stopMqtt()
    }

    fun slow() {
        controllerSpeed = ControllerSpeed.Slow
    }
    fun fast() {
        controllerSpeed = ControllerSpeed.Fast
    }
    fun mixed() {
        controllerSpeed = ControllerSpeed.Mixed
    }
}