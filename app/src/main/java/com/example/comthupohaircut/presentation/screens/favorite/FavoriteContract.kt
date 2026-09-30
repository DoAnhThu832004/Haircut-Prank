package com.example.comthupohaircut.presentation.screens.favorite

import com.example.comthupohaircut.domain.model.Sound

sealed interface FavoriteUiState {
    data object Loading: FavoriteUiState
    data object Empty: FavoriteUiState
    data class Success(val sounds: List<Sound>): FavoriteUiState
}