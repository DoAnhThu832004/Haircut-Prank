package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.repository.SoundRepository
import com.example.comthupohaircut.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSoundsByCategoryUseCase @Inject constructor(
    private val soundRepository: SoundRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(categoryName: String): Flow<List<Sound>> {
        return soundRepository.getSoundsByCategory(categoryName).map { list ->
            val soundsWithTag = list.map { sound ->
                val isKnown = userPreferencesRepository.isSoundKnown(sound.stableKey)
                sound.copy(isNew = !isKnown)
            }
            val (newSounds, otherSounds) = soundsWithTag.partition { it.isNew }
            newSounds + otherSounds
        }
    }
}