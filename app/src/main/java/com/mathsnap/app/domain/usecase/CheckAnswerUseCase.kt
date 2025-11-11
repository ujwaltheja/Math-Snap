package com.mathsnap.app.domain.usecase

import com.mathsnap.app.domain.model.MathProblem
import com.mathsnap.app.domain.model.ProblemAttempt
import javax.inject.Inject

class CheckAnswerUseCase @Inject constructor() {

    operator fun invoke(
        problem: MathProblem,
        userAnswer: Int,
        timeSpentMs: Long,
        hintsUsed: Int = 0
    ): ProblemAttempt {
        val isCorrect = userAnswer == problem.correctAnswer

        return ProblemAttempt(
            problemId = problem.id,
            userAnswer = userAnswer,
            isCorrect = isCorrect,
            timeSpentMs = timeSpentMs,
            hintsUsed = hintsUsed
        )
    }

    fun generateHint(problem: MathProblem): String {
        return when (problem.operation) {
            com.mathsnap.app.domain.model.MathOperation.ADDITION -> {
                "Try breaking ${problem.operand2} into smaller parts and adding step by step!"
            }
            com.mathsnap.app.domain.model.MathOperation.SUBTRACTION -> {
                "Think: ${problem.operand1} take away ${problem.operand2} equals what?"
            }
            com.mathsnap.app.domain.model.MathOperation.MULTIPLICATION -> {
                "Remember: ${problem.operand1} groups of ${problem.operand2}"
            }
            com.mathsnap.app.domain.model.MathOperation.DIVISION -> {
                "How many times does ${problem.operand2} fit into ${problem.operand1}?"
            }
        }
    }
}
