package com.example.comthupohaircut.presentation.screens.home

import androidx.annotation.DrawableRes
import com.example.comthupohaircut.R

object CategoryResourceMapper {
    @DrawableRes
    fun getLocalBackground(categoryKey: String): Int? {
        return when (categoryKey) {
            "hair_clipper" -> R.drawable.bg_hair_clipper
            "air_horn" -> R.drawable.bg_air_horn
            "breaking" -> R.drawable.bg_breaking
            "fart" -> R.drawable.bg_fart
            "burp" -> R.drawable.bg_burp
            "toilet_flushing" -> R.drawable.bg_toilet_flushing
            "gun" -> R.drawable.bg_gun
            "car" -> R.drawable.bg_car
            "meme" -> R.drawable.bg_meme
            "siren" -> R.drawable.bg_animals
            "taser" -> R.drawable.bg_hair_clipper
            "scary" -> R.drawable.bg_breaking
            "animals" -> R.drawable.bg_bomb
            else -> null
        }
    }
    @DrawableRes
    fun getLocalIcon(categoryKey: String): Int? {
        return when (categoryKey) {
            "hair_clipper" -> R.drawable.ic_hair
            "air_horn" -> R.drawable.ic_air
            "breaking" -> R.drawable.ic_breaking
            "fart" -> R.drawable.ic_fart
            "burp" -> R.drawable.ic_burp
            "toilet_flushing" -> R.drawable.ic_toilet
            "gun" -> R.drawable.ic_gun
            "car" -> R.drawable.ic_car
            "meme" -> R.drawable.ic_meme
            "siren" -> R.drawable.ic_police
            else -> null
        }
    }
}