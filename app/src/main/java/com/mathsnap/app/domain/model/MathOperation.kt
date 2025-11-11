package com.mathsnap.app.domain.model

enum class MathOperation(val symbol: String, val displayName: String) {
    ADDITION("+", "Addition"),
    SUBTRACTION("-", "Subtraction"),
    MULTIPLICATION("×", "Multiplication"),
    DIVISION("÷", "Division")
}

enum class DifficultyLevel {
    EASY,     // Single digit numbers
    MEDIUM,   // Double digit numbers
    HARD,     // Larger numbers, decimals
    EXPERT    // Complex problems, negatives
}

data class MathProblem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val operation: MathOperation,
    val operand1: Int,
    val operand2: Int,
    val correctAnswer: Int,
    val difficulty: DifficultyLevel,
    val timeCreated: Long = System.currentTimeMillis()
) {
    val problemText: String
        get() = "$operand1 ${operation.symbol} $operand2 = ?"
}

data class ProblemAttempt(
    val problemId: String,
    val userAnswer: Int,
    val isCorrect: Boolean,
    val timeSpentMs: Long,
    val hintsUsed: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
