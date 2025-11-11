package com.mathsnap.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey
    val badgeType: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val isEarned: Boolean = false,
    val earnedAt: Long? = null
)
