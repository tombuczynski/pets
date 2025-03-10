package com.packt.pets.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Tom Buczynski on 24.11.2024.
 */
@Serializable
data class Cat(
    @SerialName("id")
    val id: String,

    val tags: List<String>,

    val isFavorite: Boolean = false,

    val createdAt: String = "",
    val updatedAt: String = "",

    val owner: String = "",
)