package com.example.comthupohaircut.data.util

import android.content.Context
import com.example.comthupohaircut.data.local.entity.CategoryEntity
import com.example.comthupohaircut.data.local.entity.SoundEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.inject.Inject
import javax.inject.Singleton

enum class AssetState {
    EXTRACTED, ALREADY_READY, FAILED
}

@Singleton
class SoundAssetManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val filesDir = context.filesDir
    private val soundAssetDir = File(filesDir, Constants.NAME_FILE)
    val soundCategoryDir = File(soundAssetDir, Constants.KEY_FILE)
    private val markerFile = File(filesDir, "${Constants.NAME_FILE_ZIP}.done")

    private companion object {
        const val BUFFER_SIZE = 64 * 1024
        const val MACOSX_PREFIX = "__MACOSX"
        const val DS_STORE = ".DS_Store"
    }

    fun prepareAssets(): AssetState {
        // 1. Nếu marker tồn tại và dữ liệu toàn vẹn -> Sẵn sàng
        if (markerFile.exists() && AssetIntegrity.isExtractionComplete(soundCategoryDir)) {
            return AssetState.ALREADY_READY
        }

        // 2. Nếu thiếu file hoặc lỗi -> Giải nén lại nguyên tử (Atomic)
        markerFile.delete()
        soundAssetDir.deleteRecursively()

        return if (extractAtomically()) {
            runCatching { markerFile.writeText(Constants.NAME_FILE_ZIP) }
            AssetState.EXTRACTED
        } else {
            AssetState.FAILED
        }
    }

    private fun extractAtomically(): Boolean {
        val tmpDir = File(filesDir, "${Constants.NAME_FILE}.tmp")
        tmpDir.deleteRecursively()
        return try {
            unzipFromAssets(Constants.NAME_FILE_ZIP, tmpDir)
            val extractedRoot = File(tmpDir, Constants.NAME_FILE).takeIf { it.isDirectory } ?: tmpDir
            val extractedCategoryDir = File(extractedRoot, Constants.KEY_FILE)
            if (!AssetIntegrity.isExtractionComplete(extractedCategoryDir)) {
                throw IllegalStateException("Giải nén thất bại: Dữ liệu không đầy đủ")
            }
            soundAssetDir.deleteRecursively()
            soundAssetDir.parentFile?.mkdirs()
            if (!extractedRoot.renameTo(soundAssetDir)) {
                extractedRoot.copyRecursively(soundAssetDir, overwrite = true)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            soundAssetDir.deleteRecursively()
            false
        } finally {
            tmpDir.deleteRecursively()
        }
    }

    private fun unzipFromAssets(zipFileName: String, outputFolder: File) {
        if (!outputFolder.exists()) outputFolder.mkdirs()
        val canonicalRoot = outputFolder.canonicalPath
        val buffer = ByteArray(BUFFER_SIZE)
        ZipInputStream(context.assets.open(zipFileName).buffered(BUFFER_SIZE)).use { zip ->
            var entry: ZipEntry? = zip.nextEntry
            while (entry != null) {
                val name = entry.name
                if (name.contains(MACOSX_PREFIX) || name.endsWith(DS_STORE) || name.contains("/._") || name.startsWith("._")) {
                    zip.closeEntry()
                    entry = zip.nextEntry
                    continue
                }
                val newFile = File(outputFolder, name)
                if (!newFile.canonicalPath.startsWith(canonicalRoot)) {
                    throw SecurityException("Zip entry ngoài thư mục đích: $name")
                }
                if (entry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile?.takeIf { !it.exists() }?.mkdirs()
                    BufferedOutputStream(FileOutputStream(newFile), BUFFER_SIZE).use { out ->
                        var len: Int
                        while (zip.read(buffer).also { len = it } > 0) {
                            out.write(buffer, 0, len)
                        }
                    }
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }
    }

    /**
     * Quét thư mục sau khi giải nén và chuyển thành List Entities cho Room
     */
    fun parseCategoriesAndSounds(
        oldFavorites: Map<String, Long>
    ): Pair<List<CategoryEntity>, List<SoundEntity>> {
        val categories = mutableListOf<CategoryEntity>()
        val sounds = mutableListOf<SoundEntity>()

        if (!soundCategoryDir.exists() || !soundCategoryDir.isDirectory) {
            return Pair(emptyList(), emptyList())
        }

        soundCategoryDir.listFiles()?.forEach { categoryDir ->
            if (!AssetIntegrity.isCategoryDir(categoryDir)) return@forEach

            val parts = categoryDir.name.split("_")
            val numberId = parts[0].toIntOrNull() ?: return@forEach
            val categoryName = parts.subList(1, parts.size).joinToString(" ").replace("_", " ").trim().capitalizeWords()

            val category = CategoryEntity(
                id = numberId,
                nameCategory = categoryName,
                textColor = AssetIntegrity.readTextColor(File(categoryDir, AssetIntegrity.FILE_COLOR)),
                backgroundPath = AssetIntegrity.pathIfExists(categoryDir, "background.png") ?: "",
                iconImage = AssetIntegrity.pathIfExists(categoryDir, AssetIntegrity.FILE_ICON) ?: ""
            )
            categories.add(category)

            categoryDir.walkTopDown().forEach { soundFile ->
                if (soundFile.isFile && soundFile.extension.equals("mp3", ignoreCase = true)) {
                    val numberStr = soundFile.name.substringAfter(" ").substringBefore(".")
                    val soundId = numberStr.toIntOrNull() ?: 0
                    val soundName = soundFile.nameWithoutExtension.replace("_", " ").trim().capitalizeWords()
                    val stableKey = "$categoryName|$soundName"
                    val favTime = oldFavorites[stableKey] ?: 0L
                    val isFav = favTime > 0L

                    sounds.add(
                        SoundEntity(
                            id = soundId,
                            idCategory = categoryName,
                            name = soundName,
                            pathSound = soundFile.absolutePath,
                            iconPath = category.iconImage ?: "",
                            checkFavorite = isFav,
                            favoriteTime = favTime
                        )
                    )
                }
            }
        }
        return Pair(categories, sounds)
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { s ->
        s.lowercase(Locale.getDefault()).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}
