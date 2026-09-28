package com.example.comthupohaircut.domain.repository

import com.example.comthupohaircut.domain.model.SoundCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<SoundCategory>>
    suspend fun insertAll(categories: List<SoundCategory>)
}