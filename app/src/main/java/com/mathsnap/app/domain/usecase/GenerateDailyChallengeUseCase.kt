package com.mathsnap.app.domain.usecase

import com.mathsnap.app.domain.model.DailyChallenge
import com.mathsnap.app.domain.model.DifficultyLevel
import com.mathsnap.app.domain.model.MathOperation
import com.mathsnap.app.domain.model.MathProblem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GenerateDailyChallengeUseCase @Inject constructor(
    private val generateProblem: GenerateProblemUseCase
) {

    operator fun invoke(date: String = getCurrentDate()): DailyChallenge {
        val problems = mutableListOf<MathProblem>()

        // Generate 10 problems with mixed operations and difficulties
        MathOperation.values().forEach { operation ->
            // 2 easy, 1 medium per operation
            problems.add(generateProblem(operation, DifficultyLevel.EASY))
            problems.add(generateProblem(operation, DifficultyLevel.EASY))
            problems.add(generateProblem(operation, DifficultyLevel.MEDIUM))
        }

        // Add 2 hard problems (mixed operations)
        problems.add(generateProblem(MathOperation.MULTIPLICATION, DifficultyLevel.HARD))
        problems.add(generateProblem(MathOperation.ADDITION, DifficultyLevel.HARD))

        return DailyChallenge(
            date = date,
            problems = problems.shuffled()
        )
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
