package io.moxd.dreair.model.mqtt

import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class Mqtt(private val host: String) {

    fun subscribe(topic: String): Flow<MqttResult> {
        return callbackFlow {
            val client =
                MqttClient(host, MqttAsyncClient.generateClientId(), MemoryPersistence()).apply {
                    connect()
                }

            client.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    cancel()
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    trySend(MqttResult(topic ?: "", message?.toString() ?: ""))
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                }
            })

            client.subscribe(topic)

            awaitClose { // called when "cancel" is invoked, or if the collector is cancelled (like .take(1))
                client.disconnect()
                client.close()
            }
        }
    }

}

data class MqttResult(val topic: String, val message: String)
val fakeMqttResult = MqttResult("drea", "1657268275416,3,-3.14,-20")