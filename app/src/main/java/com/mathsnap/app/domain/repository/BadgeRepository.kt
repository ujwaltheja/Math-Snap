package com.mathsnap.app.domain.repository

import com.mathsnap.app.domain.model.Badge
import com.mathsnap.app.domain.model.BadgeType
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {

    suspend fun initializeBadges()

    fun getAllBadges(): Flow<List<Badge>>

    fun getEarnedBadges(): Flow<List<Badge>>

    suspend fun earnBadge(badgeType: BadgeType, timestamp: Long = System.currentTimeMillis())

    suspend fun getEarnedBadgeTypes(): Set<BadgeType>

    fun getEarnedBadgeCount(): Flow<Int>
}
