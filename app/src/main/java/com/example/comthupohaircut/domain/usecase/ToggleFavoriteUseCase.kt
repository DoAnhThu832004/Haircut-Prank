package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.repository.SoundRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val soundRepository: SoundRepository
) {
    suspend operator fun invoke(sound: Sound) {
        val newFavoriteState = !sound.checkFavorite
        val newFavTime = if(newFavoriteState) System.currentTimeMillis() else 0L
        soundRepository.updateFavorite(
            path = sound.pathSound,
            isFavorite = newFavoriteState,
            favTime = newFavTime
        )
    }
}