package com.example.comthupohaircut.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")
    object Main : Screen("main")
}