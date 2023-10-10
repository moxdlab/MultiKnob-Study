package io.moxd.dreair.model.mqtt

import io.moxd.dreair.model.persistence.MqttData
import org.junit.Assert.*
import org.junit.Test

class MqttExtensionsKtTest {

    @Test
    fun `should parse input correctly`() {
        val message = MqttResult("drea", "1657268275416,3,-3.14,-20")
        val actualMqttData = message.toMqttData()
        val expectedMqttData = MqttData(1657268275416, 3, -3.14f, -20f)
        assertEquals(expectedMqttData, actualMqttData)
    }

    @Test
    fun `should return null when spaces are used`() {
        val message = MqttResult("drea", "1657268275416, 3, -3.14, -20.3")
        assertNull(message.toMqttData())
    }

    @Test
    fun `should return null for non-matching types`() {
        val message = MqttResult("drea", "1657268275416.3,3.0,-3.0,-20.0")
        assertNull(message.toMqttData())
    }

    @Test
    fun `should return null for messages that cannot be parsed`() {
        val message = MqttResult("drea", "temp:23")
        assertNull(message.toMqttData())
    }
}