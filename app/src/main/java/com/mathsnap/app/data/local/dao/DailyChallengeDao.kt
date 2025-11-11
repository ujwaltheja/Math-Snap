package com.mathsnap.app.data.local.dao

import androidx.room.*
import com.mathsnap.app.data.local.entity.DailyChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChallengeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: DailyChallengeEntity)

    @Query("SELECT * FROM daily_challenges WHERE date = :date")
    fun getChallengeByDate(date: String): Flow<DailyChallengeEntity?>

    @Query("SELECT * FROM daily_challenges WHERE date = :date")
    suspend fun getChallengeSyncByDate(date: String): DailyChallengeEntity?

    @Query("SELECT * FROM daily_challenges ORDER BY date DESC LIMIT :limit")
    fun getRecentChallenges(limit: Int = 30): Flow<List<DailyChallengeEntity>>

    @Query("UPDATE daily_challenges SET isCompleted = 1, score = :score, completedAt = :timestamp WHERE date = :date")
    suspend fun completeChallenge(date: String, score: Int, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM daily_challenges WHERE isCompleted = 1")
    fun getCompletedChallengeCount(): Flow<Int>
}
