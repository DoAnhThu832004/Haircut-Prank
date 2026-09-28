package com.example.comthupohaircut.presentation.screens.home
import androidx.compose.runtime.Immutable
import com.example.comthupohaircut.domain.model.SoundCategory

@Immutable
data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<SoundCategory> = emptyList(),
    val errorMessage: String? = null
)

sealed interface HomeUiEffect {
    data class NavigateToListSound(val categoryName: String) : HomeUiEffect
    data object NavigateToSettings : HomeUiEffect
}