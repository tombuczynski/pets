package com.packt.pets.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.packt.pets.data.Cat
import com.packt.pets.navigation.ContentType
import com.packt.pets.navigation.NavigationType
import com.packt.pets.viewmodel.PetsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Tom Buczynski on 28.12.2024.
 */

@Composable
fun FavoritePetsScreen(
    navigationType: NavigationType,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    petsViewModel: PetsViewModel
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
                onFavoritePetClicked = { petsViewModel.updatePet(it) }
            )
        }
    }
}
