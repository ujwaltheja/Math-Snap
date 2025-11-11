package com.mathsnap.app.domain.model

enum class BadgeType {
    FIRST_PROBLEM,      // Solved first problem
    STREAK_7,           // 7-day streak
    STREAK_30,          // 30-day streak
    STREAK_100,         // 100-day streak
    PERFECT_10,         // 10 correct in a row
    PERFECT_50,         // 50 correct in a row
    SPEED_DEMON,        // Complete problem in under 3 seconds
    CENTURY_CLUB,       // 100 problems solved
    THOUSAND_CLUB,      // 1000 problems solved
    MASTER_ADDITION,    // 100 addition problems correct
    MASTER_SUBTRACTION, // 100 subtraction problems correct
    MASTER_MULTIPLICATION, // 100 multiplication problems correct
    MASTER_DIVISION,    // 100 division problems correct
    NIGHT_OWL,          // Practice at night (after 10 PM)
    EARLY_BIRD         // Practice in morning (before 7 AM)
}

data class Badge(
    val type: BadgeType,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val earnedAt: Long? = null,
    val isEarned: Boolean = false
) {
    companion object {
        fun getAvailableBadges(): List<Badge> = listOf(
            Badge(BadgeType.FIRST_PROBLEM, "First Steps", "Solve your first problem", "🎯"),
            Badge(BadgeType.STREAK_7, "Week Warrior", "Maintain a 7-day streak", "🔥"),
            Badge(BadgeType.STREAK_30, "Month Master", "Maintain a 30-day streak", "🏆"),
            Badge(BadgeType.STREAK_100, "Streak Legend", "Maintain a 100-day streak", "👑"),
            Badge(BadgeType.PERFECT_10, "Perfect 10", "Get 10 problems correct in a row", "💯"),
            Badge(BadgeType.PERFECT_50, "Perfectionist", "Get 50 problems correct in a row", "🌟"),
            Badge(BadgeType.SPEED_DEMON, "Speed Demon", "Solve a problem in under 3 seconds", "⚡"),
            Badge(BadgeType.CENTURY_CLUB, "Century Club", "Solve 100 problems", "💯"),
            Badge(BadgeType.THOUSAND_CLUB, "Thousand Club", "Solve 1000 problems", "🎖️"),
            Badge(BadgeType.MASTER_ADDITION, "Addition Master", "100 correct addition problems", "➕"),
            Badge(BadgeType.MASTER_SUBTRACTION, "Subtraction Master", "100 correct subtraction problems", "➖"),
            Badge(BadgeType.MASTER_MULTIPLICATION, "Multiplication Master", "100 correct multiplication problems", "✖️"),
            Badge(BadgeType.MASTER_DIVISION, "Division Master", "100 correct division problems", "➗"),
            Badge(BadgeType.NIGHT_OWL, "Night Owl", "Practice after 10 PM", "🦉"),
            Badge(BadgeType.EARLY_BIRD, "Early Bird", "Practice before 7 AM", "🐦")
        )
    }
}

data class Reward(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val points: Int,
    val timestamp: Long = System.currentTimeMillis()
)
