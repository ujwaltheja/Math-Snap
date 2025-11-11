package com.mathsnap.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mathsnap.app.data.local.dao.*
import com.mathsnap.app.data.local.entity.*

@Database(
    entities = [
        ProblemAttemptEntity::class,
        UserProgressEntity::class,
        BadgeEntity::class,
        DailyChallengeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MathSnapDatabase : RoomDatabase() {
    abstract fun problemAttemptDao(): ProblemAttemptDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun badgeDao(): BadgeDao
    abstract fun dailyChallengeDao(): DailyChallengeDao

    companion object {
        const val DATABASE_NAME = "mathsnap_database"
    }
}
