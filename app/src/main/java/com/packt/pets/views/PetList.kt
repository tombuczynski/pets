package com.packt.pets.views

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.packt.pets.data.Cat

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PetList(
    pets: List<Cat>,
    selectedPet: Cat?,
    onPetClicked: (Cat) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onFavoritePetClicked: (Cat) -> Unit,
) {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(
            color = Color.Red.toArgb(),
        )
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        LazyColumn(modifier = modifier, state = listState) {
            items(pets, key = { pet -> pet.id }) { pet ->
                PetListItem(
                    pet = pet,
                    onPetClicked = onPetClicked,
                    onFavoritePetClicked = onFavoritePetClicked,
                    modifier = Modifier.fillMaxWidth(),
                    isSelected = selectedPet == pet,
                )
            }
        }
    }
}

/**
 * Created by Tom Buczynski on 14.12.2024.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PetListItem(
    pet: Cat,
    onPetClicked: (Cat) -> Unit,
    onFavoritePetClicked: (Cat) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val borderModifier =
        if (isSelected) {
            Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CardDefaults.elevatedShape)
        } else {
            Modifier
        }

    ElevatedCard(
//        colors =
//            CardDefaults.elevatedCardColors().copy(
//                containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Unspecified
//            ),
        modifier = modifier
            .padding(8.dp) then borderModifier
            .testTag("PetListItemCard")
            .selectable(isSelected) { }
            .combinedClickable(onLongClick = { throw UnsupportedOperationException("Long Click not implemented") }) {
                onPetClicked(pet)
            },
    ) {
        Column(modifier = Modifier.fillMaxWidth().testTag("PetListItemColumn")) {
            AsyncImage(
                model = "https://cataas.com/cat/${pet.id}",
                contentDescription = "Cute Cat",
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlowRow(modifier = Modifier.weight(1f).padding(end = 3.dp)) {
                    pet.tags.forEach {
                        if (it.isNotEmpty()) {
                            SuggestionChip(
                                modifier = Modifier.padding(horizontal = 3.dp),
                                onClick = {},
                                label = {
                                    Text(text = it)
                                },
                            )
                        }
                    }
                }
                Icon(
                    imageVector = if (pet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    tint = if (pet.isFavorite) Color.Red else Color.Gray,
                    contentDescription = "Favorite",
                    modifier = Modifier.toggleable(pet.isFavorite) {
                        onFavoritePetClicked(pet.copy(isFavorite = it))
                    },
//                    modifier = Modifier.clickable {
//                        onFavoritePetClicked(pet)
//                    },
                )
            }
        }
    }
}