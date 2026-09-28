package com.example.comthupohaircut.data.util

import android.graphics.Color
import org.json.JSONObject
import java.io.File

object AssetIntegrity {
    const val DEFAULT_TEXT_COLOR = "#FFFFFF"
    const val FILE_COLOR = "color.json"
    const val FILE_ICON = "icon.webp"

    private val REQUIRED_CATEGORY_FILES = listOf(FILE_COLOR, FILE_ICON)
    private val CATEGORY_NAME_REGEX = Regex("^\\d+_.+")

    private fun File.isUsableFile(): Boolean = isFile && length() > 0

    fun isCategoryDir(dir: File): Boolean = dir.isDirectory && CATEGORY_NAME_REGEX.matches(dir.name)

    fun isCategoryComplete(dir: File): Boolean {
        if (!isCategoryDir(dir)) return false
        if (REQUIRED_CATEGORY_FILES.any { !File(dir, it).isUsableFile() }) return false
        return dir.walkTopDown().any { it.isUsableFile() && it.extension.equals("mp3", ignoreCase = true) }
    }

    fun isExtractionComplete(soundRoot: File): Boolean {
        if (!soundRoot.isDirectory) return false
        val dirs = soundRoot.listFiles()?.filter { isCategoryDir(it) } ?: return false
        if (dirs.isEmpty()) return false
        return dirs.all { isCategoryComplete(it) }
    }

    fun readTextColor(file: File): String {
        if (!file.isUsableFile()) return DEFAULT_TEXT_COLOR
        return try {
            val json = JSONObject(file.readText())
            val colorStr = json.optString("textColor", DEFAULT_TEXT_COLOR)
            Color.parseColor(colorStr)
            colorStr
        } catch (e: Exception) {
            DEFAULT_TEXT_COLOR
        }
    }

    fun pathIfExists(dir: File, fileName: String): String? =
        File(dir, fileName).takeIf { it.isUsableFile() }?.absolutePath
}
