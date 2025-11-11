package com.mathsnap.app.data.repository

import com.mathsnap.app.data.local.dao.BadgeDao
import com.mathsnap.app.data.local.entity.BadgeEntity
import com.mathsnap.app.domain.model.Badge
import com.mathsnap.app.domain.model.BadgeType
import com.mathsnap.app.domain.repository.BadgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BadgeRepositoryImpl @Inject constructor(
    private val badgeDao: BadgeDao
) : BadgeRepository {

    override suspend fun initializeBadges() {
        val existingBadges = badgeDao.getAllBadges().first()
        if (existingBadges.isEmpty()) {
            val badgeEntities = Badge.getAvailableBadges().map { badge ->
                BadgeEntity(
                    badgeType = badge.type.name,
                    name = badge.name,
                    description = badge.description,
                    iconEmoji = badge.iconEmoji,
                    isEarned = false,
                    earnedAt = null
                )
            }
            badgeDao.insertBadges(badgeEntities)
        }
    }

    override fun getAllBadges(): Flow<List<Badge>> {
        return badgeDao.getAllBadges().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getEarnedBadges(): Flow<List<Badge>> {
        return badgeDao.getEarnedBadges().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun earnBadge(badgeType: BadgeType, timestamp: Long) {
        badgeDao.earnBadge(badgeType.name, timestamp)
    }

    override suspend fun getEarnedBadgeTypes(): Set<BadgeType> {
        val earnedBadges = badgeDao.getEarnedBadges().first()
        return earnedBadges.mapNotNull { entity ->
            try {
                BadgeType.valueOf(entity.badgeType)
            } catch (e: IllegalArgumentException) {
                null
            }
        }.toSet()
    }

    override fun getEarnedBadgeCount(): Flow<Int> {
        return badgeDao.getEarnedBadgeCount()
    }

    private fun BadgeEntity.toDomain() = Badge(
        type = BadgeType.valueOf(badgeType),
        name = name,
        description = description,
        iconEmoji = iconEmoji,
        isEarned = isEarned,
        earnedAt = earnedAt
    )
}
