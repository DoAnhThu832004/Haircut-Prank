package com.example.comthupohaircut.domain.model

data class Sound(
    val id: Int = 0,
    val idCategory: String = "",
    val pathSound: String = "",
    val name: String = "",
    val backgroundColor: Int = 0xFFFFFFFF.toInt(),
    val backgroundListColor: Int = 0xFF000000.toInt(),
    val iconPath: String = "",
    val textColor: Int = 0xFF000000.toInt(),
    val checkFavorite: Boolean = false,
    val favoriteTime: Long = 0L,
    val isNew: Boolean = false
) {
    val stableKey: String get() = "$idCategory|$name"
}
