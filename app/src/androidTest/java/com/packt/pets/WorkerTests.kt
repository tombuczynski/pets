package com.packt.pets

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import com.packt.pets.workers.SynchronizePetsWorker
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.workmanager.factory.KoinWorkerFactory

/**
 * Created by Tom Buczynski on 11.02.2025.
 */
@RunWith(AndroidJUnit4::class)
class WorkerTests {
    private lateinit var appContext: Context

//    @get:Rule

    @Before
    fun setup() {
        appContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testSynchronizePetsWorker() {
        val worker = TestListenableWorkerBuilder<SynchronizePetsWorker>(appContext)
            .setWorkerFactory(KoinWorkerFactory())
            .build()

        runTest {
            val result = worker.doWork()

            assertThat(result, `is`(Result.success()))
        }
    }
}