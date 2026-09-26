package com.example.comthupohaircut.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.comthupohaircut.presentation.screens.intro.IntroScreen
import com.example.comthupohaircut.presentation.screens.main.MainScreen
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
            IntroScreen()
        }
        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}