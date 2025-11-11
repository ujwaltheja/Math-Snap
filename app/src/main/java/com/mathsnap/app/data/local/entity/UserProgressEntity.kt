package com.mathsnap.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val userId: String = "default_user",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalProblemsSolved: Int = 0,
    val totalCorrect: Int = 0,
    val totalIncorrect: Int = 0,
    val lastPracticeDate: Long? = null,
    val totalPracticeTimeMs: Long = 0L
)
