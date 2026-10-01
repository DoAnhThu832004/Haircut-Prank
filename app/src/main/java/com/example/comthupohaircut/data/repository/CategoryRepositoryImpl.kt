package com.example.comthupohaircut.data.repository

import com.example.comthupohaircut.data.local.dao.CategoryDao
import com.example.comthupohaircut.data.local.entity.CategoryEntity
import com.example.comthupohaircut.domain.model.SoundCategory
import com.example.comthupohaircut.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<SoundCategory>> =
        categoryDao.getAllCategories().map { list -> list.map { it.toDomain() } }

    override suspend fun insertAll(categories: List<SoundCategory>) = withContext(Dispatchers.IO) {
        categoryDao.insertAll(categories.map { CategoryEntity.fromDomain(it) })
    }
}