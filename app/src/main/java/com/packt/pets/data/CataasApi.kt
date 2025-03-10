package com.packt.pets.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Tom Buczynski on 23.11.2024.
 */

interface CataasApi {
    @GET("cats")
    suspend fun fetchCats(
        @Query("tags") tag: String,
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0,
    ): Response<List<Cat>>
}