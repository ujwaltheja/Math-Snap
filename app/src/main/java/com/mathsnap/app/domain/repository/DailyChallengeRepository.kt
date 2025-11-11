package com.mathsnap.app.domain.repository

import com.mathsnap.app.domain.model.DailyChallenge
import kotlinx.coroutines.flow.Flow

interface DailyChallengeRepository {

    suspend fun getTodayChallenge(): DailyChallenge

    fun getChallengeByDate(date: String): Flow<DailyChallenge?>

    suspend fun saveChallenge(challenge: DailyChallenge)

    suspend fun completeChallenge(date: String, score: Int)

    fun getRecentChallenges(limit: Int = 30): Flow<List<DailyChallenge>>

    fun getCompletedChallengeCount(): Flow<Int>
}
