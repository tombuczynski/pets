package com.packt.pets

import android.app.Application
import com.packt.pets.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Tom Buczynski on 10.02.2025.
 */
class PetsTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext)
            //workManagerFactory()

            modules(appModules)
        }
    }
}
