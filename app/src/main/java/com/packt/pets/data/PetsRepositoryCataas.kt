package com.packt.pets.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

private const val ITEMS_COUNT = 20

/**
 * Created by Tom Buczynski on 26.11.2024.
 */
class PetsRepositoryCataas(
    private val api: CataasApi,
    private val dao: CatDao,
) : PetsRepository {
    override suspend fun getAllPets(): Flow<NetworkResult<List<Cat>>> {
        return withContext(Dispatchers.IO) {
            val catsFLow = dao.getAllCats()

            catsFLow.map { catList ->
                catList.map {
                    Cat(
                        id = it.id,
                        tags = it.tags,
                        isFavorite = it.isFavorite,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        owner = it.owner,
                    )
                }
            }.asNetworkResult().map { result ->
                if (result is NetworkResult.Success) {
                    try {
                        if (result.data.isEmpty()) {
                            fetchPetsRemotely()
                        }
                        result
                    } catch (e: IOException) {
                        NetworkResult.Error(e.message ?: "Unknown error")
                    }
                } else {
                    result
                }
            }
        }
    }

    override suspend fun getFavoritePets(): Flow<List<Cat>> {
        return withContext(Dispatchers.IO) {
            val catsFLow = dao.getFavoriteCats()

            catsFLow.map { catList ->
                catList.map {
                    Cat(
                        id = it.id,
                        tags = it.tags,
                        isFavorite = it.isFavorite,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        owner = it.owner,
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
            owner = pet.owner,
        )

        dao.update(cat)
    }

    override suspend fun fetchPetsRemotely() {
        withContext(Dispatchers.IO) {
            val response = api.fetchCats("cute", ITEMS_COUNT)

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
                            owner = it.owner,
                        ),
                    )
                }
            } else {
                throw IOException(response.errorBody().toString())
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