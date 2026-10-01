package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.repository.SoundRepository
import javax.inject.Inject

class GetSoundByPathUseCase @Inject constructor(
    private val soundRepository: SoundRepository
) {
    suspend operator fun invoke(path: String): Sound? {
        return soundRepository.getSoundByPath(path)
    }
}