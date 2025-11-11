package com.mathsnap.app.data.local.dao

import androidx.room.*
import com.mathsnap.app.data.local.entity.ProblemAttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemAttemptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ProblemAttemptEntity): Long

    @Query("SELECT * FROM problem_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<ProblemAttemptEntity>>

    @Query("SELECT * FROM problem_attempts WHERE operation = :operation ORDER BY timestamp DESC")
    fun getAttemptsByOperation(operation: String): Flow<List<ProblemAttemptEntity>>

    @Query("SELECT * FROM problem_attempts WHERE isCorrect = 1 ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCorrectAttempts(limit: Int = 10): Flow<List<ProblemAttemptEntity>>

    @Query("SELECT COUNT(*) FROM problem_attempts WHERE isCorrect = 1 AND operation = :operation")
    suspend fun getCorrectCountByOperation(operation: String): Int

    @Query("SELECT AVG(timeSpentMs) FROM problem_attempts WHERE operation = :operation AND isCorrect = 1")
    suspend fun getAverageTimeByOperation(operation: String): Long?

    @Query("SELECT * FROM problem_attempts WHERE DATE(timestamp/1000, 'unixepoch') = DATE('now') ORDER BY timestamp DESC")
    fun getTodayAttempts(): Flow<List<ProblemAttemptEntity>>

    @Query("DELETE FROM problem_attempts")
    suspend fun deleteAll()
}
