package com.example.comthupohaircut.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")
    object Main : Screen("main")

    data object ListSound : Screen("list_sound/{categoryName}") {
        fun createRoute(categoryName: String) = "list_sound/$categoryName"
    }

    data object Setting : Screen("setting")
}