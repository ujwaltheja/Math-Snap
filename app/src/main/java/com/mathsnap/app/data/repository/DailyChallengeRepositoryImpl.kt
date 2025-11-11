package com.mathsnap.app.data.repository

import com.mathsnap.app.data.local.dao.DailyChallengeDao
import com.mathsnap.app.data.local.entity.DailyChallengeEntity
import com.mathsnap.app.domain.model.DailyChallenge
import com.mathsnap.app.domain.repository.DailyChallengeRepository
import com.mathsnap.app.domain.usecase.GenerateDailyChallengeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyChallengeRepositoryImpl @Inject constructor(
    private val dailyChallengeDao: DailyChallengeDao,
    private val generateDailyChallenge: GenerateDailyChallengeUseCase
) : DailyChallengeRepository {

    override suspend fun getTodayChallenge(): DailyChallenge {
        val today = getCurrentDate()
        val existingChallenge = dailyChallengeDao.getChallengeSyncByDate(today)

        return if (existingChallenge != null) {
            // Return existing challenge (problem IDs would be used to regenerate problems)
            generateDailyChallenge(today)
        } else {
            // Generate new challenge
            val newChallenge = generateDailyChallenge(today)
            saveChallenge(newChallenge)
            newChallenge
        }
    }

    override fun getChallengeByDate(date: String): Flow<DailyChallenge?> {
        return dailyChallengeDao.getChallengeByDate(date).map { entity ->
            entity?.let {
                generateDailyChallenge(date) // Regenerate problems for the date
            }
        }
    }

    override suspend fun saveChallenge(challenge: DailyChallenge) {
        val entity = DailyChallengeEntity(
            date = challenge.date,
            problemIds = challenge.problems.joinToString(",") { it.id },
            isCompleted = challenge.isCompleted,
            score = challenge.score,
            completedAt = challenge.completedAt
        )
        dailyChallengeDao.insertChallenge(entity)
    }

    override suspend fun completeChallenge(date: String, score: Int) {
        dailyChallengeDao.completeChallenge(date, score)
    }

    override fun getRecentChallenges(limit: Int): Flow<List<DailyChallenge>> {
        return dailyChallengeDao.getRecentChallenges(limit).map { entities ->
            entities.map { entity ->
                DailyChallenge(
                    date = entity.date,
                    problems = emptyList(), // Would need to regenerate
                    isCompleted = entity.isCompleted,
                    score = entity.score,
                    completedAt = entity.completedAt
                )
            }
        }
    }

    override fun getCompletedChallengeCount(): Flow<Int> {
        return dailyChallengeDao.getCompletedChallengeCount()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
