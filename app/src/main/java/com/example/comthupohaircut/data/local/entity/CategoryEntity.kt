package com.example.comthupohaircut.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.comthupohaircut.domain.model.SoundCategory

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameCategory: String? = "",
    val textColor: String? = "#FFFFFF",
    val backgroundPath: String? = "",
    val iconImage: String? = ""
) {
    fun toDomain(): SoundCategory = SoundCategory(
        id = id,
        name = nameCategory.orEmpty(),
        textColor = textColor ?: "#FFFFFF",
        backgroundPath = backgroundPath.orEmpty(),
        iconImage = iconImage.orEmpty()
    )

    companion object {
        fun fromDomain(category: SoundCategory): CategoryEntity = CategoryEntity(
            id = category.id,
            nameCategory = category.name,
            textColor = category.textColor,
            backgroundPath = category.backgroundPath,
            iconImage = category.iconImage
        )
    }
}