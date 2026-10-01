package com.example.comthupohaircut.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")
    object Main : Screen("main")

    data object ListSound : Screen("list_sound/{categoryName}") {
        fun createRoute(categoryName: String) = "list_sound/$categoryName"
    }

    data object Setting : Screen("setting")

    data object DetailSound : Screen("detail_sound/{categoryName}/{soundPath}") {
        fun createRoute(categoryName: String, soundPath: String): String {
            val encodedPath = java.net.URLEncoder.encode(soundPath, "UTF-8")
            return "detail_sound/$categoryName/$encodedPath"
        }
    }
}