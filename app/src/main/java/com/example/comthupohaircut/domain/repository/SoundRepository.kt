package com.example.comthupohaircut.domain.repository

import com.example.comthupohaircut.domain.model.Sound
import kotlinx.coroutines.flow.Flow

interface SoundRepository {
    fun getAllSounds(): Flow<List<Sound>>
    fun getSoundsByCategory(categoryId: String): Flow<List<Sound>>
    fun getFavoriteSounds(): Flow<List<Sound>>
    suspend fun getSoundByPath(path: String): Sound?
    suspend fun updateFavorite(path: String, isFavorite: Boolean, favTime: Long)
    suspend fun prepareInitialData(): Result<Unit>
}