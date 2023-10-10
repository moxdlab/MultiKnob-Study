package io.moxd.dreair.model.study

import io.moxd.dreair.model.mqtt.Mqtt
import io.moxd.dreair.model.mqtt.fakeMqttResult
import io.moxd.dreair.model.persistence.MqttDataDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class MqttHelperTest {

    @Test
    fun `mqtt callback should be invoked on correct incoming mqtt message`() = runTest {

        //given
        val daoMock = mock<MqttDataDao>()
        val connMock = mock<Mqtt> {
            on { subscribe(any()) } doReturn flowOf(fakeMqttResult)
        }
        val mqttHelper = MqttHelper(daoMock, connMock)

        //when
        var failed = true
        mqttHelper.startMqtt { _, _ -> failed = false }

        //then
        if (failed) fail("expected calling callback, but didn't happen")
        mqttHelper.stopMqtt()
    }

    @Test
    fun `finish persisting, even if mqtt is stopped already`() = runTest {

        var failed = true
        val daoMock = mock<MqttDataDao> {
            onBlocking {
                insert(any())
            }.doSuspendableAnswer {
                delay(1000) //any suspension time will suffice (being cooperative)
                //until here we're cancelled if the SUT is not using different scopes
                failed = false
                1
            }
        }

        val connMock = mock<Mqtt> {
            on { subscribe(any()) } doReturn flowOf(fakeMqttResult)
        }
        val mqttHelper = MqttHelper(daoMock, connMock)

        mqttHelper.startMqtt { _, _ ->
            mqttHelper.stopMqtt()
        }

        if (failed) fail()
    }
}