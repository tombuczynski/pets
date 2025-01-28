package com.packt.pets.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.json.Json

/**
 * Created by Tom Buczynski on 24.01.2025.
 */

const val PETS_DATABASE_NAME = "pets_database"

class PetsDBTypeConverters {

    @TypeConverter
    fun convertTagListToString(tags: List<String>): String =
        Json.encodeToString(tags)

    @TypeConverter
    fun convertStringToTagList(string: String): List<String> =
        Json.decodeFromString(string)

}

@Database(entities = [CatEntity::class], version = 1)
@TypeConverters(PetsDBTypeConverters::class)
abstract class PetsDatabase : RoomDatabase() {
    abstract fun getCatDao(): CatDao
}
