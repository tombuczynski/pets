package com.packt.pets.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.packt.pets.data.Cat

/**
 * Created by Tom Buczynski on 29.12.2024.
 */

@Composable
fun PetDetailsScreen(pet: Cat, modifier: Modifier = Modifier) {
    PetDetails(pet, modifier)
}