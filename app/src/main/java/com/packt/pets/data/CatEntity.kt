package com.packt.pets.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Tom Buczynski on 24.01.2025.
 */
@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: String,

    val tags: List<String>,

    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
    val owner: String,
)