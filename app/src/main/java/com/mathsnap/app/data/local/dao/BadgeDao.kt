package com.mathsnap.app.data.local.dao

import androidx.room.*
import com.mathsnap.app.data.local.entity.BadgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Query("SELECT * FROM badges ORDER BY earnedAt DESC")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges WHERE isEarned = 1 ORDER BY earnedAt DESC")
    fun getEarnedBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges WHERE badgeType = :badgeType")
    suspend fun getBadge(badgeType: String): BadgeEntity?

    @Query("UPDATE badges SET isEarned = 1, earnedAt = :timestamp WHERE badgeType = :badgeType")
    suspend fun earnBadge(badgeType: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM badges WHERE isEarned = 1")
    fun getEarnedBadgeCount(): Flow<Int>
}
