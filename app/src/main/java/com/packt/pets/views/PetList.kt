package com.packt.pets.views

import android.graphics.drawable.PaintDrawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.DrawableImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import com.packt.pets.data.Cat

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PetList(
    pets: List<Cat>,
    onPetClicked: (Cat) -> Unit,
    modifier: Modifier = Modifier,
    selectedPet: Cat? = null,
    listState: LazyListState = rememberLazyListState()
) {
    val previewHandler = AsyncImagePreviewHandler {
        FakeImage(
            color = Color.Red.toArgb()
        )
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {

        LazyColumn(modifier = modifier, state = listState) {
            items(pets) { pet ->
                PetListItem(
                    pet = pet,
                    modifier = Modifier.fillMaxWidth(),
                    onPetClicked = onPetClicked,
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
    modifier: Modifier = Modifier,
    onPetClicked: (Cat) -> Unit,
    isSelected: Boolean = false
) {
    ElevatedCard(
        colors =
            CardDefaults.elevatedCardColors().copy(
                containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Unspecified
            ),
        modifier = modifier
            .padding(8.dp)
            .selectable(isSelected) { onPetClicked(pet) }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = "https://cataas.com/cat/${pet.id}",
                contentDescription = "Cute Cat",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.height(10.dp))
            FlowRow(modifier = Modifier.padding(horizontal = 6.dp)) {
                pet.tags.forEach {
                    if (it.isNotEmpty()) {
                        SuggestionChip(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            onClick = {},
                            label = {
                                Text(text = it)
                            }
                        )
                    }
                }
            }
        }
    }
}