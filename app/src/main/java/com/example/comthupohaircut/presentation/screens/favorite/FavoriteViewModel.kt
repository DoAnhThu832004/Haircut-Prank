package com.example.comthupohaircut.presentation.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.usecase.GetFavoriteSoundsUseCase
import com.example.comthupohaircut.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoriteSoundsUseCase: GetFavoriteSoundsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
): ViewModel() {
    val uiState: StateFlow<FavoriteUiState> = getFavoriteSoundsUseCase()
        .map { sounds ->
            if(sounds.isEmpty()) {
                FavoriteUiState.Empty
            } else {
                FavoriteUiState.Success(sounds)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FavoriteUiState.Loading
        )
    fun onToggleFavorite(sound: Sound) {
        viewModelScope.launch {
            toggleFavoriteUseCase(sound)
        }
    }
}