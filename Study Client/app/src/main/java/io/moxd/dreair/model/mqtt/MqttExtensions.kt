package io.moxd.dreair.model.mqtt

import io.moxd.dreair.model.controller.ControllerState
import io.moxd.dreair.model.persistence.MqttData

/**
 * Converts mqtt messages with correct format:
 * {timestamp},{fingerCount},{currentRotation},{rotationSum}.
 *
 * All other messages result in returning null.
 */
fun MqttResult.toMqttData(): MqttData? {
    if (message.isEmpty() || message.lowercase().contains("none")) return null
    val values = message.split(",")
    return try {
        MqttData(values[0].toLong(), values[1].toInt(), values[2].toFloat(), values[3].toFloat())
    } catch (e: Exception) {
        null //ignoring any messages that cannot be parsed
    }
}

fun MqttData.toControllerState() =
    ControllerState(fingerCount, rotation, rotationSum)