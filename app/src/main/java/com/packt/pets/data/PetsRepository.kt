package com.packt.pets.data

import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    suspend fun getAllPets(): Flow<NetworkResult<List<Cat>>>
    suspend fun getFavoritePets(): Flow<List<Cat>>
    suspend fun updatePet(pet: Cat)

    suspend fun fetchPetsRemotely()
}