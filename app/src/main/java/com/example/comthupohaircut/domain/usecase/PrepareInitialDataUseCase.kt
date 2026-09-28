package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.data.local.dao.CategoryDao
import com.example.comthupohaircut.data.local.dao.SoundDao
import com.example.comthupohaircut.data.local.pref.AppPreferences
import com.example.comthupohaircut.data.util.AssetState
import com.example.comthupohaircut.data.util.Constants
import com.example.comthupohaircut.data.util.SoundAssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrepareInitialDataUseCase @Inject constructor(
    private val assetManager: SoundAssetManager,
    private val soundDao: SoundDao,
    private val categoryDao: CategoryDao,
    private val appPreferences: AppPreferences
) {
    suspend operator fun invoke(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val state = assetManager.prepareAssets()
            when (state) {
                AssetState.EXTRACTED -> {
                    // 1. Lưu lại các Favorite từ phiên trước theo stableKey ("category|soundName")
                    val oldSounds = soundDao.getAllSoundsSync()
                    val oldFavorites = HashMap<String, Long>()
                    val oldKeys = HashSet<String>()
                    oldSounds.forEach { old ->
                        val key = "${old.idCategory ?: ""}|${old.name}"
                        oldKeys.add(key)
                        if (old.checkFavorite) {
                            oldFavorites[key] = old.favoriteTime
                        }
                    }

                    // 2. Parse dữ liệu mới từ folder assets đã giải nén
                    val (categories, sounds) = assetManager.parseCategoriesAndSounds(oldFavorites)

                    // 3. Ghi vào Room DB
                    categoryDao.deleteAll()
                    soundDao.deleteAll()
                    if (categories.isNotEmpty()) categoryDao.insertAll(categories)
                    if (sounds.isNotEmpty()) soundDao.insertAll(sounds)

                    // 4. Cập nhật baseline tag "NEW"
                    if (!appPreferences.isSoundBaselineDone()) {
                        val knownKeys = if (oldKeys.isEmpty()) {
                            sounds.map { "${it.idCategory ?: ""}|${it.name}" }
                                .filter { it !in Constants.NEW_SOUND_KEYS }
                        } else {
                            oldKeys
                        }
                        appPreferences.markSoundsKnown(knownKeys)
                        appPreferences.setSoundBaselineDone(true)
                    }
                }
                AssetState.ALREADY_READY -> {
                    // Dữ liệu đã có sẵn, chỉ cần đảm bảo baseline
                    if (!appPreferences.isSoundBaselineDone()) {
                        val sounds = soundDao.getAllSoundsSync()
                        val keys = sounds.map { "${it.idCategory ?: ""}|${it.name}" }
                            .filter { it !in Constants.NEW_SOUND_KEYS }
                        appPreferences.markSoundsKnown(keys)
                        appPreferences.setSoundBaselineDone(true)
                    }
                }
                AssetState.FAILED -> {
                    throw IllegalStateException("Không thể chuẩn bị dữ liệu âm thanh từ file Zip.")
                }
            }

            // Trả về cờ kiểm tra xem có cần chuyển tới màn Intro không
            !appPreferences.isIntroDone()
        }
    }
}