package com.mathsnap.app.domain.usecase

import com.mathsnap.app.domain.model.DifficultyLevel
import com.mathsnap.app.domain.model.MathOperation
import com.mathsnap.app.domain.model.MathProblem
import javax.inject.Inject
import kotlin.random.Random

class GenerateProblemUseCase @Inject constructor() {

    operator fun invoke(
        operation: MathOperation,
        difficulty: DifficultyLevel
    ): MathProblem {
        val (operand1, operand2) = generateOperands(difficulty)

        val (finalOp1, finalOp2, answer) = when (operation) {
            MathOperation.ADDITION -> {
                Triple(operand1, operand2, operand1 + operand2)
            }
            MathOperation.SUBTRACTION -> {
                // Ensure positive result for easier levels
                val larger = maxOf(operand1, operand2)
                val smaller = minOf(operand1, operand2)
                Triple(larger, smaller, larger - smaller)
            }
            MathOperation.MULTIPLICATION -> {
                Triple(operand1, operand2, operand1 * operand2)
            }
            MathOperation.DIVISION -> {
                // Generate division problems with whole number answers
                val divisor = if (operand2 == 0) 1 else operand2
                val quotient = operand1
                val dividend = quotient * divisor
                Triple(dividend, divisor, quotient)
            }
        }

        return MathProblem(
            operation = operation,
            operand1 = finalOp1,
            operand2 = finalOp2,
            correctAnswer = answer,
            difficulty = difficulty
        )
    }

    private fun generateOperands(difficulty: DifficultyLevel): Pair<Int, Int> {
        return when (difficulty) {
            DifficultyLevel.EASY -> {
                Pair(Random.nextInt(1, 11), Random.nextInt(1, 11))
            }
            DifficultyLevel.MEDIUM -> {
                Pair(Random.nextInt(10, 51), Random.nextInt(10, 51))
            }
            DifficultyLevel.HARD -> {
                Pair(Random.nextInt(50, 101), Random.nextInt(50, 101))
            }
            DifficultyLevel.EXPERT -> {
                Pair(Random.nextInt(100, 501), Random.nextInt(100, 501))
            }
        }
    }
}
