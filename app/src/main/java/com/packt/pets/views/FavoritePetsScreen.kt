package com.packt.pets.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.packt.pets.viewmodel.PetsViewModel

/**
 * Created by Tom Buczynski on 28.12.2024.
 */

@Composable
fun FavoritePetsScreen(
    listState: LazyListState,
    petsViewModel: PetsViewModel,
    modifier: Modifier = Modifier,
) {
    val pets by petsViewModel.favoritePetList.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        if (pets.isEmpty()) {
            Text("No Favorite Pets yet !")
        } else {
            FavoritePetList(
                pets = pets,
                modifier = Modifier.fillMaxSize(),
                listState = listState,
                onFavoritePetClicked = { petsViewModel.updatePet(it) },
            )
        }
    }
}