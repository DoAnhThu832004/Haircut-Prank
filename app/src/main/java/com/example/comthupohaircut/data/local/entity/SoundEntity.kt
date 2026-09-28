package com.example.comthupohaircut.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.comthupohaircut.domain.model.Sound

@Entity(tableName = "sound")
data class SoundEntity(
    val id: Int? = null,
    val idCategory: String? = null,
    @PrimaryKey val pathSound: String = "",
    val name: String = "",
    val backgroundColor: Int = 0xFFFFFFFF.toInt(),
    val backgroundListColor: Int = 0xFF000000.toInt(),
    val iconPath: String = "",
    val textColor: Int = 0xFF000000.toInt(),
    val checkFavorite: Boolean = false,
    val favoriteTime: Long = 0L
) {
    val stableKey: String get() = "${idCategory.orEmpty()}|$name"

    fun toDomain(): Sound = Sound(
        id = id ?: 0,
        idCategory = idCategory.orEmpty(),
        pathSound = pathSound,
        name = name,
        backgroundColor = backgroundColor,
        backgroundListColor = backgroundListColor,
        iconPath = iconPath,
        textColor = textColor,
        checkFavorite = checkFavorite,
        favoriteTime = favoriteTime
    )

    companion object {
        fun fromDomain(sound: Sound): SoundEntity = SoundEntity(
            id = sound.id,
            idCategory = sound.idCategory,
            pathSound = sound.pathSound,
            name = sound.name,
            backgroundColor = sound.backgroundColor,
            backgroundListColor = sound.backgroundListColor,
            iconPath = sound.iconPath,
            textColor = sound.textColor,
            checkFavorite = sound.checkFavorite,
            favoriteTime = sound.favoriteTime
        )
    }
}
