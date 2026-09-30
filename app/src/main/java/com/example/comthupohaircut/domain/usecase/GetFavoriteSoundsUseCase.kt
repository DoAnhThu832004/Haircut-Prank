package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.repository.SoundRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteSoundsUseCase @Inject constructor(
    private val soundRepository: SoundRepository
) {
    operator fun invoke() : Flow<List<Sound>> {
        return soundRepository.getFavoriteSounds()
    }
}