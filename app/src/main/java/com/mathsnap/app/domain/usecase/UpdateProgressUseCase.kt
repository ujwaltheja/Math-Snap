package com.mathsnap.app.domain.usecase

import com.mathsnap.app.domain.model.ProblemAttempt
import com.mathsnap.app.domain.repository.ProgressRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {

    suspend operator fun invoke(attempt: ProblemAttempt) {
        // Save the attempt
        progressRepository.saveAttempt(attempt)

        // Update overall progress
        val currentProgress = progressRepository.getUserProgress()

        // Check if this is a new day
        val today = System.currentTimeMillis()
        val lastPractice = currentProgress?.lastPracticeDate ?: 0L
        val daysSinceLastPractice = TimeUnit.MILLISECONDS.toDays(today - lastPractice)

        val newStreak = when {
            daysSinceLastPractice == 0L -> currentProgress?.currentStreak ?: 1
            daysSinceLastPractice == 1L -> (currentProgress?.currentStreak ?: 0) + 1
            else -> 1 // Reset streak
        }

        progressRepository.updateProgress(
            totalProblems = (currentProgress?.totalProblemsSolved ?: 0) + 1,
            correctCount = if (attempt.isCorrect) (currentProgress?.totalCorrect ?: 0) + 1
                          else currentProgress?.totalCorrect ?: 0,
            incorrectCount = if (!attempt.isCorrect) (currentProgress?.totalIncorrect ?: 0) + 1
                            else currentProgress?.totalIncorrect ?: 0,
            currentStreak = newStreak,
            longestStreak = maxOf(newStreak, currentProgress?.longestStreak ?: 0),
            lastPracticeDate = today,
            totalPracticeTime = (currentProgress?.totalPracticeTimeMs ?: 0) + attempt.timeSpentMs
        )
    }
}
