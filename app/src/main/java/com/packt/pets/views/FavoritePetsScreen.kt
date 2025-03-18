package com.packt.pets.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.packt.pets.data.Cat

/**
 * Created by Tom Buczynski on 28.12.2024.
 */

@Composable
fun FavoritePetsScreen(
    favoritePets: List<Cat>,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    onFavoritePetClicked: (Cat) -> Unit = {},
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        if (favoritePets.isEmpty()) {
            Text("No Favorite Pets yet !")
        } else {
            FavoritePetList(
                pets = favoritePets,
                modifier = Modifier.fillMaxSize(),
                listState = listState,
                onFavoritePetClicked = onFavoritePetClicked,
            )
        }
    }
}