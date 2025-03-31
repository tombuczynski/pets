package com.packt.pets.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Query("SELECT * FROM cats")
    fun getAllCats(): Flow<List<CatEntity>>

    @Query("SELECT * FROM cats WHERE is_favorite = 1")
    fun getFavoriteCats(): Flow<List<CatEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(cat: CatEntity)

    @Update
    suspend fun update(cat: CatEntity)
}