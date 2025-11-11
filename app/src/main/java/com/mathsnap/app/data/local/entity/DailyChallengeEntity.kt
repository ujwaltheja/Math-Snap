package com.mathsnap.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey
    val date: String, // YYYY-MM-DD format
    val problemIds: String, // Comma-separated problem IDs
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val completedAt: Long? = null
)
