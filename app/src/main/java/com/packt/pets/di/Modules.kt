package com.packt.pets.di

import androidx.room.Room
import com.packt.pets.data.CatDao
import com.packt.pets.data.CataasApi
import com.packt.pets.data.PETS_DATABASE_NAME
import com.packt.pets.data.PetsDatabase
import com.packt.pets.data.PetsRepository
import com.packt.pets.data.PetsRepositoryCataas
import com.packt.pets.data.PetsRepositoryDemo
import com.packt.pets.viewmodel.PetsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val appModules = module {
    single<PetsRepository> { PetsRepositoryCataas(get(), get(), get()) }

    single<CoroutineDispatcher> { Dispatchers.IO }

    single<PetsViewModel> { PetsViewModel(get()) }

    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .baseUrl("https://cataas.com/api/")
            .build()
    }

    single<CataasApi> { get<Retrofit>().create(CataasApi::class.java) }

    single<PetsDatabase> {
        Room.databaseBuilder(
            androidContext(),
            PetsDatabase::class.java,
            PETS_DATABASE_NAME
        ).build()
    }

    single<CatDao> { get<PetsDatabase>().getCatDao() }
}

val appPreviewModules = module {
    single<PetsRepository> { PetsRepositoryDemo() }

    single<PetsViewModel> { PetsViewModel(get()) }
}