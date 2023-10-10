package io.moxd.dreair.model.study

import io.moxd.dreair.BASE_TOPIC
import io.moxd.dreair.HOST
import io.moxd.dreair.model.controller.ControllerState
import io.moxd.dreair.model.mqtt.Mqtt
import io.moxd.dreair.model.mqtt.toControllerState
import io.moxd.dreair.model.mqtt.toMqttData
import io.moxd.dreair.model.persistence.MqttData
import io.moxd.dreair.model.persistence.MqttDataDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.cancellable

class MqttHelper(
    private val mqttDao: MqttDataDao? = null,
    private val mqttConn: Mqtt = Mqtt(HOST)
) {
    private var mqttJob: Job? = null

    suspend fun startMqtt(onData: (mqttData: MqttData, cs: ControllerState) -> Unit) {
        mqttJob?.cancelAndJoin()
        coroutineScope {
            val outerScope = this
            mqttJob = launch {
                mqttConn.subscribe(BASE_TOPIC).cancellable().collect { mqttRes ->
                    val mqttData = mqttRes.toMqttData()?.also { outerScope.persistMqtt(it) }
                        ?: return@collect
                    val controllerState = mqttData.toControllerState()
                    onData(mqttData, controllerState)
                }
            }
        }
    }

    fun stopMqtt(cause: String = "") {
        mqttJob?.cancel(cause)
    }

    private fun CoroutineScope.persistMqtt(mqttData: MqttData) {
        launch(Dispatchers.IO) {
            mqttDao?.insert(mqttData)
        }
    }
}
