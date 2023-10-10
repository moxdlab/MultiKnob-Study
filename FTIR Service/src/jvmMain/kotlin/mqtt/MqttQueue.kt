package mqtt

import concurrency.BaseConcurrency
import kotlinx.coroutines.Dispatchers

object MqttQueue {

    private val mqtt = Mqtt()

    private val concurrency = object : BaseConcurrency<String>(
        dispatcher = Dispatchers.IO,
        concurrentWorkers = 1, //single worker to keep order
        workerAction = ::publishMqtt
    ) {}

    fun scheduleMqtt(message: String) {
        concurrency.addWorkElement(message)
    }

    private fun publishMqtt(message: String) {
        mqtt.publish(message)
    }
}
