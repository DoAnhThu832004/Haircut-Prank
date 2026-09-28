package com.example.comthupohaircut.di

import android.content.Context
import androidx.room.Room
import com.example.comthupohaircut.data.local.AppDatabase
import com.example.comthupohaircut.data.local.dao.CategoryDao
import com.example.comthupohaircut.data.local.dao.SoundDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "haircut_prank.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideSoundDao(database: AppDatabase): SoundDao {
        return database.soundDao()
    }
}