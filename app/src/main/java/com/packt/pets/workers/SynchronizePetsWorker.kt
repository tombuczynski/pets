package com.packt.pets.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.packt.pets.data.PetsRepository
import java.io.IOException

/**
 * Created by Tom Buczynski on 03.02.2025.
 */
class SynchronizePetsWorker(
    context: Context,
    params: WorkerParameters,
    private val repo: PetsRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result =
        try {
            repo.fetchPetsRemotely()
            Result.success()
        } catch (e: IOException) {
            //Log.e("SynchronizePetsWorker", "doWork: " + e.message)
            Result.retry()
        }
}