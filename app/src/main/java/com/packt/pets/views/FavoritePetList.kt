package com.packt.pets.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.packt.pets.data.Cat

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun FavoritePetList(
    @PreviewParameter(PetListPreviewParameterProvider::class) pets: List<Cat>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onFavoritePetClicked: (Cat) -> Unit = { },
) {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(
            color = Color.Blue.toArgb(),
            height = 800,
        )
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        LazyColumn(modifier = modifier, state = listState) {
            items(pets, key = { it.id }) { pet ->
                FavoritePetListItem(
                    pet = pet,
                    modifier = Modifier.fillMaxWidth(),
                    // .animateItem(),
                    onFavoritePetClicked = onFavoritePetClicked,
                )
            }
        }
    }
}

class PetListPreviewParameterProvider : PreviewParameterProvider<List<Cat>> {
    override val values: Sequence<List<Cat>>
        get() = sequenceOf(
            listOf(
                Cat(
                    "GUOunrpLPB6Jw0zE",
                    listOf(
                        "handicapped",
                        "tabby",
                        "double",
                        "multiple",
                    ),
                    true,
                ),
                Cat("dmjubveyQIbCFA2v", listOf("orange")),
            ),
        )
}

@Composable
fun FavoritePetListItem(
    pet: Cat,
    modifier: Modifier = Modifier,
    onFavoritePetClicked: (Cat) -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
        ) {
            AsyncImage(
                model = "https://cataas.com/cat/${pet.id}",
                contentDescription = "Cute Cat",
                modifier = Modifier
//                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Icon(
                imageVector = if (pet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                tint = if (pet.isFavorite) Color.Red else Color.Gray,
                contentDescription = "Favorite",
                modifier = Modifier.padding(4.dp).clickable {
                    onFavoritePetClicked(pet)
                },
            )
        }
    }
}