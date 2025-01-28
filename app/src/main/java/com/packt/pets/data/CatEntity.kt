package com.packt.pets.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Tom Buczynski on 24.01.2025.
 */
@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: String,

    val tags: List<String>,

    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,

    val owner: String,
)

@Dao
interface CatDao {
    @Query("SELECT * FROM cats")
    fun getAll(): Flow<List<CatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cat: CatEntity)
}