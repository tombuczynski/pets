package com.packt.pets.navigation

import com.packt.pets.data.Cat
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Created by Tom Buczynski on 31.12.2024.
 */
@Serializable
sealed class Route {

    @Serializable
    data object Pets : Route()

    @Serializable
    data object FavoritePets : Route()

    @Serializable
    data class PetDetails(val serializedPet: String) : Route() {
        constructor(pet: Cat) : this(Json.encodeToString(pet))

        val pet: Cat
            get() = Json.decodeFromString(serializedPet)
    }
}
