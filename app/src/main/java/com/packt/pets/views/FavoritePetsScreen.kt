package com.packt.pets.views

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.packt.pets.data.Cat
import com.packt.pets.navigation.ContentType
import com.packt.pets.navigation.NavigationType

/**
 * Created by Tom Buczynski on 28.12.2024.
 */

@Composable
fun FavoritePetsScreen(
    navigationType: NavigationType,
    modifier: Modifier = Modifier,
    onPetClicked: (Cat) -> Unit = {},
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Text("Favorite Pets")
    }
}
