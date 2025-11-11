package com.mathsnap.app.di

import com.mathsnap.app.data.repository.BadgeRepositoryImpl
import com.mathsnap.app.data.repository.DailyChallengeRepositoryImpl
import com.mathsnap.app.data.repository.ProgressRepositoryImpl
import com.mathsnap.app.domain.repository.BadgeRepository
import com.mathsnap.app.domain.repository.DailyChallengeRepository
import com.mathsnap.app.domain.repository.ProgressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        impl: ProgressRepositoryImpl
    ): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindBadgeRepository(
        impl: BadgeRepositoryImpl
    ): BadgeRepository

    @Binds
    @Singleton
    abstract fun bindDailyChallengeRepository(
        impl: DailyChallengeRepositoryImpl
    ): DailyChallengeRepository
}
