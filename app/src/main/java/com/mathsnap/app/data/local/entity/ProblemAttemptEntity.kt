package com.mathsnap.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathsnap.app.domain.model.MathOperation
import com.mathsnap.app.domain.model.DifficultyLevel

@Entity(tableName = "problem_attempts")
data class ProblemAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val problemId: String,
    val operation: String,
    val difficulty: String,
    val operand1: Int,
    val operand2: Int,
    val correctAnswer: Int,
    val userAnswer: Int,
    val isCorrect: Boolean,
    val timeSpentMs: Long,
    val hintsUsed: Int,
    val timestamp: Long
)
