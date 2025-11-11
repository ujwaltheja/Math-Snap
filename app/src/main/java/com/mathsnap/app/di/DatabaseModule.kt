package com.mathsnap.app.di

import android.content.Context
import androidx.room.Room
import com.mathsnap.app.data.local.MathSnapDatabase
import com.mathsnap.app.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMathSnapDatabase(
        @ApplicationContext context: Context
    ): MathSnapDatabase {
        return Room.databaseBuilder(
            context,
            MathSnapDatabase::class.java,
            MathSnapDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProblemAttemptDao(database: MathSnapDatabase): ProblemAttemptDao {
        return database.problemAttemptDao()
    }

    @Provides
    @Singleton
    fun provideUserProgressDao(database: MathSnapDatabase): UserProgressDao {
        return database.userProgressDao()
    }

    @Provides
    @Singleton
    fun provideBadgeDao(database: MathSnapDatabase): BadgeDao {
        return database.badgeDao()
    }

    @Provides
    @Singleton
    fun provideDailyChallengeDao(database: MathSnapDatabase): DailyChallengeDao {
        return database.dailyChallengeDao()
    }
}
