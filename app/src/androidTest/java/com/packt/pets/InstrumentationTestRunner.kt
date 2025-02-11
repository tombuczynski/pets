package com.packt.pets

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Created by Tom Buczynski on 10.02.2025.
 */
class InstrumentationTestRunner  : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, PetsTestApplication::class.java.name, context)
    }
}