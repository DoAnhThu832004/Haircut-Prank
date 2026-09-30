package com.example.comthupohaircut.presentation.screens.listsound

import com.example.comthupohaircut.domain.model.Sound

sealed interface ListSoundState {
    data object Loading: ListSoundState
    data class Success(
        val categoryName: String,
        val sounds: List<Sound>
    ): ListSoundState
    data class Error(val message: String): ListSoundState
}
sealed interface ListSoundEffect {
    data class NavigateToDetail(val sound: Sound): ListSoundEffect
    data object NavigateBack: ListSoundEffect
}