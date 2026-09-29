package com.example.comthupohaircut.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comthupohaircut.data.local.dao.CategoryDao
import com.example.comthupohaircut.data.local.dao.SoundDao
import com.example.comthupohaircut.data.local.entity.CategoryEntity
import com.example.comthupohaircut.data.local.entity.SoundEntity

@Database(
    entities = [
        CategoryEntity::class,
        SoundEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun soundDao(): SoundDao
}