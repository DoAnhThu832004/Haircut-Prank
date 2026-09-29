package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.repository.SoundRepository
import com.example.comthupohaircut.domain.repository.UserPreferencesRepository
import javax.inject.Inject
class PrepareInitialDataUseCase @Inject constructor(
    private val soundRepository: SoundRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return soundRepository.prepareInitialData().map {
            !userPreferencesRepository.isIntroDone()
        }
    }
}