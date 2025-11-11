package com.mathsnap.app.domain.model

data class UserProgress(
    val userId: String = "default_user",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalProblemsSolved: Int = 0,
    val totalCorrect: Int = 0,
    val totalIncorrect: Int = 0,
    val averageAccuracy: Float = 0f,
    val averageSpeedMs: Long = 0L,
    val lastPracticeDate: Long? = null,
    val totalPracticeTimeMs: Long = 0L
) {
    val accuracyPercentage: Int
        get() = if (totalProblemsSolved > 0) {
            ((totalCorrect.toFloat() / totalProblemsSolved) * 100).toInt()
        } else 0
}

data class OperationStats(
    val operation: MathOperation,
    val totalAttempts: Int = 0,
    val correctAttempts: Int = 0,
    val averageTimeMs: Long = 0L,
    val fastestTimeMs: Long = Long.MAX_VALUE
) {
    val accuracy: Float
        get() = if (totalAttempts > 0) {
            (correctAttempts.toFloat() / totalAttempts) * 100
        } else 0f
}

data class DailyChallenge(
    val id: String = java.util.UUID.randomUUID().toString(),
    val date: String, // YYYY-MM-DD format
    val problems: List<MathProblem>,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val completedAt: Long? = null
)
