package com.packt.pets.di

import androidx.room.Room
import androidx.work.WorkManager
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.packt.pets.data.CatDao
import com.packt.pets.data.CataasApi
import com.packt.pets.data.PETS_DATABASE_NAME
import com.packt.pets.data.PetsDatabase
import com.packt.pets.data.PetsRepository
import com.packt.pets.data.PetsRepositoryCataas
import com.packt.pets.data.PetsRepositoryDemo
import com.packt.pets.viewmodel.PetsViewModel
import com.packt.pets.workers.SynchronizePetsWorker
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val appModules = module {
    viewModelOf(::PetsViewModel)

    single<PetsRepository> { PetsRepositoryCataas(get(), get()) }

    single<ChuckerInterceptor> {
        // Create the Collector
        val chuckerCollector = ChuckerCollector(
            context = androidContext(),
            // Toggles visibility of the notification
            showNotification = true,
            // Allows to customize the retention period of collected data
            retentionPeriod = RetentionManager.Period.ONE_HOUR,
        )

        // Create the Interceptor
        ChuckerInterceptor.Builder(androidContext())
            // The previously created Collector
            .collector(chuckerCollector)
            // The max body content length in bytes, after this responses will be truncated.
            .maxContentLength(250_000L)
            // If true - read the whole response body even when the client does not consume the response completely.
            // This is useful in case of parsing errors or when the response body
            // is closed before being read like in Retrofit with Void and Unit types.
            .alwaysReadResponseBody(false)
            // Controls Android shortcut creation.
            .createShortcut(true)
            .build()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<ChuckerInterceptor>())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .client(get())
            .baseUrl("https://cataas.com/api/")
            .build()
    }

    single<CataasApi> { get<Retrofit>().create(CataasApi::class.java) }

    single<PetsDatabase> {
        Room.databaseBuilder(
            androidContext(),
            PetsDatabase::class.java,
            PETS_DATABASE_NAME,
        ).build()
    }

    single<CatDao> { get<PetsDatabase>().getCatDao() }

    single<WorkManager?> { WorkManager.getInstance(androidContext()) }

    workerOf(::SynchronizePetsWorker)
}

val appComposePreviewModules = module {
    single<PetsRepository> { PetsRepositoryDemo() }

    single<PetsViewModel> { PetsViewModel(get(), null) }
}