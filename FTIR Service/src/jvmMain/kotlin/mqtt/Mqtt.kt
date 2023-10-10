package mqtt

import kotlinx.coroutines.runBlocking
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class Mqtt(private val host: String = "tcp://localhost") {

    private val client by lazy {
        MqttClient(host, MqttClient.generateClientId()).apply {
            connect()
        }
    }

    fun publish(message: String, topic: String = "drea") {
        val mqttMessage = MqttMessage(message.toByteArray())
        client.publish(topic, mqttMessage)
    }

    fun close() {
        client.disconnect()
        client.close()
    }
}

fun main() = runBlocking {
    val mqtt = Mqtt()
    mqtt.publish("Test :)")
    mqtt.close()
}