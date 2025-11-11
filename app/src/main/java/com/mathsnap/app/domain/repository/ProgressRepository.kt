package com.mathsnap.app.domain.repository

import com.mathsnap.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    suspend fun saveAttempt(attempt: ProblemAttempt)

    fun getAllAttempts(): Flow<List<ProblemAttempt>>

    fun getAttemptsByOperation(operation: MathOperation): Flow<List<ProblemAttempt>>

    suspend fun getUserProgress(): UserProgress?

    fun getUserProgressFlow(): Flow<UserProgress?>

    suspend fun updateProgress(
        totalProblems: Int,
        correctCount: Int,
        incorrectCount: Int,
        currentStreak: Int,
        longestStreak: Int,
        lastPracticeDate: Long,
        totalPracticeTime: Long
    )

    suspend fun getCorrectCountByOperation(operation: MathOperation): Int

    fun getOperationStats(operation: MathOperation): Flow<OperationStats>
}
