package com.mathsnap.app

import android.app.Application
import com.mathsnap.app.domain.repository.BadgeRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MathSnapApplication : Application() {

    @Inject
    lateinit var badgeRepository: BadgeRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        // Initialize badges on app start
        applicationScope.launch {
            badgeRepository.initializeBadges()
        }
    }
}
