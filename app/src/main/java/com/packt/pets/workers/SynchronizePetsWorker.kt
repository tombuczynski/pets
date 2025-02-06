package com.packt.pets.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.packt.pets.data.CataasApi
import com.packt.pets.data.PetsDatabase
import com.packt.pets.data.PetsRepository
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by Tom Buczynski on 03.02.2025.
 */
class SynchronizePetsWorker(
    context: Context,
    params: WorkerParameters,
    private val repo: PetsRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result =
        try {
            repo.fetchPetsRemotely()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
}

