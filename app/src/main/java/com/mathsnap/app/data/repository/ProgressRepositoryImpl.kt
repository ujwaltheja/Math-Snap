package com.mathsnap.app.data.repository

import com.mathsnap.app.data.local.dao.ProblemAttemptDao
import com.mathsnap.app.data.local.dao.UserProgressDao
import com.mathsnap.app.data.local.entity.ProblemAttemptEntity
import com.mathsnap.app.data.local.entity.UserProgressEntity
import com.mathsnap.app.domain.model.*
import com.mathsnap.app.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val problemAttemptDao: ProblemAttemptDao,
    private val userProgressDao: UserProgressDao
) : ProgressRepository {

    override suspend fun saveAttempt(attempt: ProblemAttempt) {
        // This would normally need the full problem data
        // For now, we'll create a minimal entity
        val entity = ProblemAttemptEntity(
            problemId = attempt.problemId,
            operation = "", // Would be populated from problem
            difficulty = "",
            operand1 = 0,
            operand2 = 0,
            correctAnswer = 0,
            userAnswer = attempt.userAnswer,
            isCorrect = attempt.isCorrect,
            timeSpentMs = attempt.timeSpentMs,
            hintsUsed = attempt.hintsUsed,
            timestamp = attempt.timestamp
        )
        problemAttemptDao.insertAttempt(entity)
    }

    override fun getAllAttempts(): Flow<List<ProblemAttempt>> {
        return problemAttemptDao.getAllAttempts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAttemptsByOperation(operation: MathOperation): Flow<List<ProblemAttempt>> {
        return problemAttemptDao.getAttemptsByOperation(operation.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getUserProgress(): UserProgress? {
        return userProgressDao.getProgressSync()?.toDomain()
    }

    override fun getUserProgressFlow(): Flow<UserProgress?> {
        return userProgressDao.getProgress().map { it?.toDomain() }
    }

    override suspend fun updateProgress(
        totalProblems: Int,
        correctCount: Int,
        incorrectCount: Int,
        currentStreak: Int,
        longestStreak: Int,
        lastPracticeDate: Long,
        totalPracticeTime: Long
    ) {
        val entity = UserProgressEntity(
            totalProblemsSolved = totalProblems,
            totalCorrect = correctCount,
            totalIncorrect = incorrectCount,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastPracticeDate = lastPracticeDate,
            totalPracticeTimeMs = totalPracticeTime
        )
        userProgressDao.insertProgress(entity)
    }

    override suspend fun getCorrectCountByOperation(operation: MathOperation): Int {
        return problemAttemptDao.getCorrectCountByOperation(operation.name)
    }

    override fun getOperationStats(operation: MathOperation): Flow<OperationStats> {
        return problemAttemptDao.getAttemptsByOperation(operation.name).map { attempts ->
            val totalAttempts = attempts.size
            val correctAttempts = attempts.count { it.isCorrect }
            val averageTime = attempts.filter { it.isCorrect }
                .map { it.timeSpentMs }
                .average()
                .toLong()
            val fastestTime = attempts.filter { it.isCorrect }
                .minOfOrNull { it.timeSpentMs } ?: Long.MAX_VALUE

            OperationStats(
                operation = operation,
                totalAttempts = totalAttempts,
                correctAttempts = correctAttempts,
                averageTimeMs = averageTime,
                fastestTimeMs = fastestTime
            )
        }
    }

    private fun ProblemAttemptEntity.toDomain() = ProblemAttempt(
        problemId = problemId,
        userAnswer = userAnswer,
        isCorrect = isCorrect,
        timeSpentMs = timeSpentMs,
        hintsUsed = hintsUsed,
        timestamp = timestamp
    )

    private fun UserProgressEntity.toDomain() = UserProgress(
        userId = userId,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        totalProblemsSolved = totalProblemsSolved,
        totalCorrect = totalCorrect,
        totalIncorrect = totalIncorrect,
        lastPracticeDate = lastPracticeDate,
        totalPracticeTimeMs = totalPracticeTimeMs
    )
}
