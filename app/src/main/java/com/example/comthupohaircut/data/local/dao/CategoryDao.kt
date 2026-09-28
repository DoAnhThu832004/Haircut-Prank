package com.example.comthupohaircut.data.local.dao

import androidx.room.*
import com.example.comthupohaircut.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY id ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<CategoryEntity>)

    @Query("DELETE FROM category")
    fun deleteAll()
}