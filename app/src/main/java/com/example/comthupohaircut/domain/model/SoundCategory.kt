package com.example.comthupohaircut.domain.model

data class SoundCategory(
    val id: Int = 0,
    val name: String = "",
    val textColor: String = "#ffffff",
    val backgroundPath: String = "",
    val iconImage: String = "",
    val isNew: Boolean = false
) {
    val normalizedKey: String get() = name.lowercase().replace(" ", "_")
}