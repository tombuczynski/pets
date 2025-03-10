package com.packt.pets.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.packt.pets.data.Cat

/**
 * Created by Tom Buczynski on 29.12.2024.
 */

@Composable
fun PetDetails(pet: Cat?, modifier: Modifier = Modifier) {
    if (pet == null) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.padding(8.dp)) {
            Text(
                text = "Select the pet whose photo you want to display",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    } else {
        AsyncImage(
            model = "https://cataas.com/cat/${pet.id}",
            contentDescription = "Cute Cat",
            modifier = modifier,
            contentScale = ContentScale.Fit,
        )
    }
}