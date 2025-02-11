package com.packt.pets

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.packt.pets.workers.SynchronizePetsWorker
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Tom Buczynski on 10.02.2025.
 */
@RunWith(AndroidJUnit4::class)
class WorkManagerTests {
    private lateinit var appContext: Context

//    @get:Rule

    @Before
    fun setup() {
        appContext = ApplicationProvider.getApplicationContext()

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(KoinWorkerFactory())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(appContext, config)
    }

    @Test
    @Throws(Exception::class)
    fun testSynchronizePetsWorker() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()


        val workRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<SynchronizePetsWorker>()
                .setConstraints(constraints)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .build()

        val workManager = WorkManager.getInstance(appContext)

        val testDriver = WorkManagerTestInitHelper.getTestDriver(appContext)!!

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor
        workManager.enqueueUniqueWork(
            uniqueWorkName = "SynchronizePets",
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = workRequest
        ).result.get()

        var workInfos = workManager.getWorkInfosForUniqueWork("SynchronizePets").get()
        assertThat(workInfos.first().state, `is`(WorkInfo.State.ENQUEUED))

        testDriver.setAllConstraintsMet(workRequest.id)
        testDriver.setInitialDelayMet(workRequest.id)

        workInfos = workManager.getWorkInfosForUniqueWork("SynchronizePets").get()
        assertThat(workInfos.first().state, `is`(WorkInfo.State.RUNNING))

    }
}