package com.example.comthupohaircut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.comthupohaircut.presentation.screens.detail.DetailSoundScreen
import com.example.comthupohaircut.presentation.screens.intro.IntroScreen
import com.example.comthupohaircut.presentation.screens.intro.IntroViewModel
import com.example.comthupohaircut.presentation.screens.listsound.ListSoundScreen
import com.example.comthupohaircut.presentation.screens.main.MainScreen
import com.example.comthupohaircut.presentation.screens.setting.SettingScreen
import com.example.comthupohaircut.presentation.screens.splash.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                viewModel = hiltViewModel(),
                onNavigateNext = { navigationToIntro ->
                    val destination = if (navigationToIntro) Screen.Intro.route else Screen.Main.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true}
                    }
                }
            )
        }
        composable(Screen.Intro.route) {
            IntroScreen(
                viewModel = hiltViewModel(),
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
                onNavigateToListSound = { categoryName ->
                    navController.navigate(Screen.ListSound.createRoute(categoryName))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Setting.route)
                }
            )
        }
        composable(Screen.Setting.route) {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.ListSound.route,
            arguments = listOf(
                navArgument("categoryName") {
                    type = NavType.StringType
                }
            )
        ) {
            ListSoundScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSoundClick = { sound ->
                    navController.navigate(Screen.DetailSound.createRoute(sound.idCategory, sound.pathSound))
                }
            )
        }
        composable(
            route = Screen.DetailSound.route,
            arguments = listOf(
                navArgument("categoryName") { type = NavType.StringType },
                navArgument("soundPath") { type = NavType.StringType }
            )
        ) {
            DetailSoundScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}