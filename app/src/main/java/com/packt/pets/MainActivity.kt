package com.packt.pets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.packt.pets.navigation.AppNavigation
import com.packt.pets.ui.theme.PetsTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AppWatcher.objectWatcher.expectWeaklyReachable(this, "Activity leak test")

        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics

        // enableEdgeToEdge()
        setContent {
            PetsTheme(dynamicColor = true) {
                KoinAndroidContext {
                    AppNavigation()
                }
            }
        }
    }
}