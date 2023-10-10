package io.moxd.dreair.model.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskProgress::class, MqttData::class, Session::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskProgressDao(): TaskProgressDao
    abstract fun mqttDataDao(): MqttDataDao
    abstract fun sessionDao(): SessionDao

    companion object {
        private const val DB_NAME = "irstudydata.db"

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}