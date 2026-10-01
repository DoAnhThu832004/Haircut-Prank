package com.example.comthupohaircut.data.repository

import androidx.room.withTransaction
import com.example.comthupohaircut.data.local.AppDatabase
import com.example.comthupohaircut.data.local.dao.CategoryDao
import com.example.comthupohaircut.data.local.dao.SoundDao
import com.example.comthupohaircut.data.util.AssetState
import com.example.comthupohaircut.data.util.Constants
import com.example.comthupohaircut.data.util.SoundAssetManager
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.repository.SoundRepository
import com.example.comthupohaircut.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class SoundRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val soundDao: SoundDao,
    private val categoryDao: CategoryDao,
    private val assetManager: SoundAssetManager,
    private val userPreferencesRepository: UserPreferencesRepository
) : SoundRepository {
    override fun getAllSounds(): Flow<List<Sound>> =
        soundDao.getAllSoundsFlow().map { list -> list.map { it.toDomain() } }
    override fun getSoundsByCategory(categoryId: String): Flow<List<Sound>> =
        soundDao.getSoundsByCategoryFlow(categoryId).map { list -> list.map { it.toDomain() } }
    override fun getFavoriteSounds(): Flow<List<Sound>> =
        soundDao.getFavoriteSoundsFlow().map { list -> list.map { it.toDomain() } }
    override suspend fun getSoundByPath(path: String): Sound? = withContext(Dispatchers.IO) {
        soundDao.getSoundByPath(path)?.toDomain()
    }
    override suspend fun updateFavorite(path: String, isFavorite: Boolean, favTime: Long) = withContext(Dispatchers.IO) {
        soundDao.updateFavorite(path, isFavorite, favTime)
    }
    override suspend fun prepareInitialData(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val state = assetManager.prepareAssets()
            when (state) {
                AssetState.EXTRACTED -> {
                    val oldSounds = soundDao.getAllSoundsSync()
                    val oldFavorites = HashMap<String, Long>()
                    val oldKeys = HashSet<String>()
                    oldSounds.forEach { old ->
                        val key = "${old.idCategory.orEmpty()}|${old.name}"
                        oldKeys.add(key)
                        if (old.checkFavorite) {
                            oldFavorites[key] = old.favoriteTime
                        }
                    }
                    val (categories, sounds) = assetManager.parseCategoriesAndSounds(oldFavorites)
                    // Bọc Transaction an toàn tuyệt đối
                    appDatabase.withTransaction {
                        categoryDao.deleteAll()
                        soundDao.deleteAll()
                        if (categories.isNotEmpty()) categoryDao.insertAll(categories)
                        if (sounds.isNotEmpty()) soundDao.insertAll(sounds)
                    }
                    if (!userPreferencesRepository.isSoundBaselineDone()) {
                        val knownKeys = if (oldKeys.isEmpty()) {
                            sounds.map { "${it.idCategory.orEmpty()}|${it.name}" }
                                .filter { it !in Constants.NEW_SOUND_KEYS }
                        } else {
                            oldKeys
                        }
                        userPreferencesRepository.markSoundsKnown(knownKeys)
                        userPreferencesRepository.setSoundBaselineDone(true)
                    }
                }
                AssetState.ALREADY_READY -> {
                    if (!userPreferencesRepository.isSoundBaselineDone()) {
                        val sounds = soundDao.getAllSoundsSync()
                        val keys = sounds.map { "${it.idCategory.orEmpty()}|${it.name}" }
                            .filter { it !in Constants.NEW_SOUND_KEYS }
                        userPreferencesRepository.markSoundsKnown(keys)
                        userPreferencesRepository.setSoundBaselineDone(true)
                    }
                }
                AssetState.FAILED -> {
                    throw IllegalStateException("Không thể chuẩn bị dữ liệu âm thanh từ file Zip.")
                }
            }
        }
    }
}