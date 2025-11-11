package com.mathsnap.app.data.local.dao

import androidx.room.*
import com.mathsnap.app.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgressEntity)

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    fun getProgress(userId: String = "default_user"): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    suspend fun getProgressSync(userId: String = "default_user"): UserProgressEntity?

    @Update
    suspend fun updateProgress(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET currentStreak = :streak WHERE userId = :userId")
    suspend fun updateStreak(streak: Int, userId: String = "default_user")

    @Query("UPDATE user_progress SET totalProblemsSolved = totalProblemsSolved + 1 WHERE userId = :userId")
    suspend fun incrementTotalProblems(userId: String = "default_user")

    @Query("UPDATE user_progress SET totalCorrect = totalCorrect + 1 WHERE userId = :userId")
    suspend fun incrementCorrect(userId: String = "default_user")

    @Query("UPDATE user_progress SET totalIncorrect = totalIncorrect + 1 WHERE userId = :userId")
    suspend fun incrementIncorrect(userId: String = "default_user")
}
