package io.moxd.dreair.model.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskProgressDao {
    @Insert
    suspend fun insert(taskProgress: TaskProgress)
}

@Dao
interface MqttDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mqttData: MqttData): Long
}

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(session: Session): Long

    @Query("UPDATE Session SET subject_id = :newId WHERE subject_id = :oldId")
    suspend fun updateSubjectId(oldId: Int, newId: Int)

    @Query("SELECT * FROM Session WHERE id = :id")
    suspend fun findById(id: Long): Session

    @Query("UPDATE Session SET completion_timestamp = :completionTimestamp WHERE id = :sessionId")
    suspend fun completeSession(
        sessionId: Long,
        completionTimestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM Session WHERE subject_id > 0 ORDER BY id DESC LIMIT 1")
    suspend fun getLatestSession(): Session?

    @Query("SELECT COUNT(id) FROM SESSION WHERE subject_id = :subjectId")
    suspend fun getSessionCountForSubject(subjectId: Int): Int

    @Query("SELECT Count(id) FROM Session WHERE subject_id = :subjectId AND completion_timestamp IS NOT NULL")
    suspend fun getSessionCompletedCount(subjectId: Int): Int
}