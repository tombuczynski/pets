package com.packt.pets.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PetsRepositoryDemo: PetsRepository {
    override suspend fun getAllPets(): Flow<NetworkResult<List<Cat>>> {
        return flowOf(
            NetworkResult.Success(
                listOf(
                    Cat("GUOunrpLPB6Jw0zE", listOf(
                        "handicapped",
                        "tabby",
                        "double",
                        "multiple"),
                        true),
                    Cat("dmjubveyQIbCFA2v", listOf("orange")),
        //            Pet(1, "Bella", "Dog"),
        //            Pet(2, "Luna", "Cat"),
        //            Pet(3, "Charlie", "Dog"),
        //            Pet(4, "Lucy", "Cat"),
        //            Pet(5, "Cooper", "Dog"),
        //            Pet(6, "Max", "Cat"),
        //            Pet(7, "Bailey", "Dog"),
        //            Pet(8, "Daisy", "Cat"),
        //            Pet(9, "Sadie", "Dog"),
        //            Pet(10, "Lily", "Cat"),
                )
            )
        )
    }

    override suspend fun getFavoritePets(): Flow<List<Cat>> {
        return flowOf(emptyList())
    }

    override suspend fun updatePet(pet: Cat) {
    }

    override suspend fun fetchPetsRemotely() {
    }
}