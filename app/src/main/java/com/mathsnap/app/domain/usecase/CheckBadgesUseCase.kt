package com.mathsnap.app.domain.usecase

import com.mathsnap.app.domain.model.Badge
import com.mathsnap.app.domain.model.BadgeType
import com.mathsnap.app.domain.model.MathOperation
import com.mathsnap.app.domain.repository.BadgeRepository
import com.mathsnap.app.domain.repository.ProgressRepository
import java.util.*
import javax.inject.Inject

class CheckBadgesUseCase @Inject constructor(
    private val badgeRepository: BadgeRepository,
    private val progressRepository: ProgressRepository
) {

    suspend operator fun invoke(): List<Badge> {
        val newlyEarnedBadges = mutableListOf<Badge>()
        val progress = progressRepository.getUserProgress()
        val earnedBadges = badgeRepository.getEarnedBadgeTypes()

        // Check First Problem
        if (progress?.totalProblemsSolved ?: 0 >= 1 && !earnedBadges.contains(BadgeType.FIRST_PROBLEM)) {
            badgeRepository.earnBadge(BadgeType.FIRST_PROBLEM)
            newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.FIRST_PROBLEM })
        }

        // Check Streak Badges
        val currentStreak = progress?.currentStreak ?: 0
        when {
            currentStreak >= 100 && !earnedBadges.contains(BadgeType.STREAK_100) -> {
                badgeRepository.earnBadge(BadgeType.STREAK_100)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.STREAK_100 })
            }
            currentStreak >= 30 && !earnedBadges.contains(BadgeType.STREAK_30) -> {
                badgeRepository.earnBadge(BadgeType.STREAK_30)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.STREAK_30 })
            }
            currentStreak >= 7 && !earnedBadges.contains(BadgeType.STREAK_7) -> {
                badgeRepository.earnBadge(BadgeType.STREAK_7)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.STREAK_7 })
            }
        }

        // Check Problems Solved Badges
        val totalSolved = progress?.totalProblemsSolved ?: 0
        when {
            totalSolved >= 1000 && !earnedBadges.contains(BadgeType.THOUSAND_CLUB) -> {
                badgeRepository.earnBadge(BadgeType.THOUSAND_CLUB)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.THOUSAND_CLUB })
            }
            totalSolved >= 100 && !earnedBadges.contains(BadgeType.CENTURY_CLUB) -> {
                badgeRepository.earnBadge(BadgeType.CENTURY_CLUB)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.CENTURY_CLUB })
            }
        }

        // Check Operation Mastery Badges
        checkOperationMastery(MathOperation.ADDITION, BadgeType.MASTER_ADDITION, earnedBadges, newlyEarnedBadges)
        checkOperationMastery(MathOperation.SUBTRACTION, BadgeType.MASTER_SUBTRACTION, earnedBadges, newlyEarnedBadges)
        checkOperationMastery(MathOperation.MULTIPLICATION, BadgeType.MASTER_MULTIPLICATION, earnedBadges, newlyEarnedBadges)
        checkOperationMastery(MathOperation.DIVISION, BadgeType.MASTER_DIVISION, earnedBadges, newlyEarnedBadges)

        // Check Time-based badges
        checkTimeBasedBadges(earnedBadges, newlyEarnedBadges)

        return newlyEarnedBadges
    }

    private suspend fun checkOperationMastery(
        operation: MathOperation,
        badgeType: BadgeType,
        earnedBadges: Set<BadgeType>,
        newlyEarnedBadges: MutableList<Badge>
    ) {
        if (!earnedBadges.contains(badgeType)) {
            val correctCount = progressRepository.getCorrectCountByOperation(operation)
            if (correctCount >= 100) {
                badgeRepository.earnBadge(badgeType)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == badgeType })
            }
        }
    }

    private suspend fun checkTimeBasedBadges(
        earnedBadges: Set<BadgeType>,
        newlyEarnedBadges: MutableList<Badge>
    ) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        when {
            hourOfDay >= 22 && !earnedBadges.contains(BadgeType.NIGHT_OWL) -> {
                badgeRepository.earnBadge(BadgeType.NIGHT_OWL)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.NIGHT_OWL })
            }
            hourOfDay < 7 && !earnedBadges.contains(BadgeType.EARLY_BIRD) -> {
                badgeRepository.earnBadge(BadgeType.EARLY_BIRD)
                newlyEarnedBadges.add(Badge.getAvailableBadges().first { it.type == BadgeType.EARLY_BIRD })
            }
        }
    }
}
