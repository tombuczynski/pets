package com.packt.pets.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * Created by Tom Buczynski on 26.11.2024.
 */
class PetsRepositoryCataas(
    private val api: CataasApi,
    private val disp: CoroutineDispatcher,
    private val dao: CatDao
) : PetsRepository {
    override suspend fun getAllPets(): Flow<List<Cat>> {
        return withContext(disp) {
            val catsFLow = dao.getAllCats()

            catsFLow.map { catList ->
                catList.map {
                    Cat(
                        id = it.id,
                        tags = it.tags,
                        isFavorite = it.isFavorite,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        owner = it.owner
                    )
                }
            }.onEach { catList ->
                if (catList.isEmpty())
                    fetchPetsRemotely()
            }
        }
    }

    override suspend fun getFavoritePets(): Flow<List<Cat>> {
        return withContext(disp) {
            val catsFLow = dao.getFavoriteCats()

            catsFLow.map { catList ->
                catList.map {
                    Cat(
                        id = it.id,
                        tags = it.tags,
                        isFavorite = it.isFavorite,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        owner = it.owner
                    )
                }
            }
        }
    }

    override suspend fun updatePet(pet: Cat) {
        val cat = CatEntity(
            id = pet.id,
            tags = pet.tags,
            isFavorite = pet.isFavorite,
            createdAt = pet.createdAt,
            updatedAt = pet.updatedAt,
            owner = pet.owner
        )

        dao.update(cat)
    }

    override suspend fun fetchPetsRemotely() {
        withContext(disp) {
            val response = api.fetchCats("cute", 20)

            if (response.isSuccessful) {
                val cats = response.body() ?: emptyList()
                cats.forEach {
                    dao.insert(
                        CatEntity(
                            id = it.id,
                            tags = it.tags,
                            isFavorite = it.isFavorite,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt,
                            owner = it.owner
                        )
                    )
                }
            } else {
                throw Exception(response.errorBody().toString())
            }
        }
    }
//        withContext(disp) {
//            try {
//                val response = api.fetchCats("cute")
//                if (response.isSuccessful)
//                    NetworkResult.Success(response.body() ?: emptyList())
//                else
//                    NetworkResult.Error(response.errorBody().toString())
//            } catch (e: Exception) {
//                val msg = e.message ?: "Unknown error"
//                NetworkResult.Error(msg)
//            }
//        }
}