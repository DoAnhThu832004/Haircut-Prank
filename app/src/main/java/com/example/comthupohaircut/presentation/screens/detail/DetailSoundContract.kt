package com.example.comthupohaircut.presentation.screens.detail

import com.example.comthupohaircut.domain.model.Sound

data class DetailSoundUiState(
    val currentSound: Sound? = null,
    val otherSounds: List<Sound> = emptyList(),
    val isPlaying: Boolean = false,
    val isLooping: Boolean = false,
    val isVibrationEnabled: Boolean = true,
    val isFavorite: Boolean = false,
    val selectedTimerSeconds: Int = 0, // Mốc thời gian đã chọn: 0 (Off), 5, 10, 30, 60, 300
    val countdownRemainingSeconds: Int = 0, // Thời gian đếm ngược còn lại
    val isCountingDown: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface DetailSoundUiEffect {
    data object NavigateBack : DetailSoundUiEffect
}