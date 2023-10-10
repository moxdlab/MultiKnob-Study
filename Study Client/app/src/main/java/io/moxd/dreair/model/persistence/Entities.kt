package io.moxd.dreair.model.persistence

import androidx.room.*
import io.moxd.dreair.model.controller.ControllerSpeed
import java.lang.Exception

@Entity
data class TaskProgress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "current_pos") val currentPos: Float,
    @ColumnInfo(name = "task_id") val taskId: Int,
    @ColumnInfo(name = "session_id_fk") val sessionIdFk: Long,
    @ColumnInfo(name = "mqtt_data_id_fk") val mqttDataIdFk: Long
)

@Entity
data class MqttData(
    @PrimaryKey @ColumnInfo(name = "python_timestamp") val pythonTimestamp: Long, //is sent over
    @ColumnInfo(name = "finger_count") val fingerCount: Int,
    val rotation: Float,
    @ColumnInfo(name = "rotation_sum") val rotationSum: Float
)

@Entity
data class Session(
    //each subject has 3 sessions (for all speeds one)
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "subject_id") val subjectId: Int,
    @ColumnInfo(name = "creation_timestamp") val creationTimestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "completion_timestamp") val completionTimestamp: Long? = null, //somewhat redundant, but maybe helpful for some recovery stuff
    @ColumnInfo(name = "controller_speed") val controllerSpeed: ControllerSpeed,
)

data class LatestSessionTask(
    @ColumnInfo(name = "task_id") val taskId: Int,
    @Embedded val session: Session
)

class Converters {

    @TypeConverter
    fun fromControllerSpeed(cs: ControllerSpeed): Char = cs.char

    @TypeConverter
    fun charToControllerSpeed(char: Char): ControllerSpeed = ControllerSpeed.get(char)
        ?: throw Exception("unsupported char for trying to convert to ControllerSpeed")
}