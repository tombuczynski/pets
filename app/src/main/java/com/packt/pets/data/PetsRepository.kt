package com.packt.pets.data

import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    suspend fun getPets(): Flow<List<Cat>>

    suspend fun fetchPetsRemotely()
}