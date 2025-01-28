package com.packt.pets.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Created by Tom Buczynski on 26.11.2024.
 */
class PetsRepositoryCataas(
    private val api: CataasApi,
    private val disp: CoroutineDispatcher,
    private val dao: CatDao
) : PetsRepository {
    override suspend fun getPets(): Flow<List<Cat>> {
        return withContext(disp) {
            val catsFLow = dao.getAll()

            catsFLow.map { catList ->
                catList.map {
                    Cat(
                        id = it.id,
                        tags = it.tags,
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