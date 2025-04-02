package com.packt.pets.navigation

import androidx.annotation.Keep
import com.packt.pets.data.Cat
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Created by Tom Buczynski on 31.12.2024.
 */
@Keep
@Serializable
sealed class Route {

    @Keep
    @Serializable
    data object Pets : Route()

    @Keep
    @Serializable
    data object FavoritePets : Route()

    @Keep
    @Serializable
    data class PetDetails(val serializedPet: String) : Route() {
        constructor(pet: Cat) : this(Json.encodeToString(pet))

        val pet: Cat
            get() = Json.decodeFromString(serializedPet)
    }
}