package com.example.comthupohaircut.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.comthupohaircut.data.local.entity.SoundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundDao {
    @Query("SELECT * FROM sound ORDER BY id ASC")
    fun getAllSoundsFlow(): Flow<List<SoundEntity>>

    @Query("SELECT * FROM sound")
    fun getAllSoundsSync(): List<SoundEntity>

    @Query("SELECT * FROM sound WHERE idCategory = :categoryId ORDER BY id ASC")
    fun getSoundsByCategoryFlow(categoryId: String): Flow<List<SoundEntity>>

    @Query("SELECT * FROM sound WHERE idCategory = :categoryId ORDER BY id ASC")
    fun getSoundsByCategorySync(categoryId: String): List<SoundEntity>

    @Query("SELECT * FROM sound WHERE checkFavorite = 1 ORDER BY favoriteTime DESC")
    fun getFavoriteSoundsFlow(): Flow<List<SoundEntity>>

    @Query("SELECT * FROM sound WHERE pathSound = :path LIMIT 1")
    fun getSoundByPath(path: String): SoundEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sounds: List<SoundEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sound: SoundEntity)

    @Update
    fun update(sound: SoundEntity)

    @Query("DELETE FROM sound")
    fun deleteAll()

    @Query("UPDATE sound SET checkFavorite = :isFavorite, favoriteTime = :favTime WHERE pathSound = :path")
    fun updateFavorite(path: String, isFavorite: Boolean, favTime: Long)
}
