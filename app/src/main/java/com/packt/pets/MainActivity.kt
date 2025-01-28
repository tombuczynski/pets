package com.packt.pets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.packt.pets.navigation.AppNavigation
import com.packt.pets.ui.theme.PetsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            PetsTheme(dynamicColor = true) {
                AppNavigation()
            }
        }
    }
}
