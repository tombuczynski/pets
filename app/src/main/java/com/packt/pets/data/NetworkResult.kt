package com.packt.pets.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry

/**
 * Created by Tom Buczynski on 26.11.2024.
 */
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val msg: String) : NetworkResult<Nothing>()
}

fun <T> Flow<T>.asNetworkResult(): Flow<NetworkResult<T>> {
    return this.map<T, NetworkResult<T>> {
        NetworkResult.Success(it)
    }.catch { exception ->
        emit(NetworkResult.Error(exception.message ?: "Unknown error"))
        retry()
    }
}
