package com.example.comthupohaircut.presentation.screens.splash

import androidx.compose.runtime.Immutable

// Đánh dấu @Immutable để Compose Compiler tối ưu skip recomposition
@Immutable
sealed interface SplashUiState {
    data object Loading: SplashUiState
    data class Error(val errorMessage: String): SplashUiState
}

sealed interface SplashUiEffect {
    data class NavigateNext(val navigationToIntro: Boolean): SplashUiEffect
}