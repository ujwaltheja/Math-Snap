package com.mathsnap.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mathsnap.app.ui.screens.home.HomeScreen
import com.mathsnap.app.ui.screens.practice.PracticeScreen
import com.mathsnap.app.ui.screens.progress.ProgressScreen
import com.mathsnap.app.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Practice : Screen("practice/{operation}/{difficulty}") {
        fun createRoute(operation: String, difficulty: String) =
            "practice/$operation/$difficulty"
    }
    object Progress : Screen("progress")
    object Settings : Screen("settings")
    object DailyChallenge : Screen("daily_challenge")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Practice.route,
            arguments = listOf(
                navArgument("operation") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val operation = backStackEntry.arguments?.getString("operation") ?: "ADDITION"
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "EASY"
            PracticeScreen(
                operation = operation,
                difficulty = difficulty,
                navController = navController
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
