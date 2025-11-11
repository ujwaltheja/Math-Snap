package com.mathsnap.app.domain.model

data class UserSettings(
    val isDarkMode: Boolean = false,
    val isKidMode: Boolean = false,
    val soundEffectsEnabled: Boolean = true,
    val dailyReminderEnabled: Boolean = true,
    val reminderTimeHour: Int = 9, // 9 AM default
    val isPremium: Boolean = false,
    val selectedAvatar: String = "default"
)

enum class SubscriptionTier {
    FREE,
    PREMIUM,
    MATH_MASTERY
}
