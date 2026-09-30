package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class MarkSoundsViewedUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(soundKeys: Collection<String>) {
        userPreferencesRepository.markSoundsKnown(soundKeys)
    }
}