package com.packt.pets.views

import android.graphics.Rect
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.packt.pets.data.Cat
import com.packt.pets.di.appComposePreviewModules
import com.packt.pets.navigation.ContentType
import com.packt.pets.navigation.NavControlType
import com.packt.pets.navigation.NavigationType
import com.packt.pets.viewmodel.PetListUIState
import com.packt.pets.viewmodel.PetsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

/**
 * Created by Tom Buczynski on 28.12.2024.
 */

@Composable
fun PetsScreen(
    navigationType: NavigationType,
    uiState: PetListUIState,
    selectedPet: Cat?,
    //petsViewModel: PetsViewModel,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    onPetClicked: (Cat) -> Unit,
    onFavoritePetClicked: (Cat) -> Unit,
) {

//    val petsViewModel: PetsViewModel = viewModel()

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        AnimatedVisibility(visible = uiState.isLoading) {
            CircularProgressIndicator()
        }
        AnimatedVisibility(visible = uiState.pets.isNotEmpty()) {

            var rightPanelWidth = 0.dp
            if (navigationType.featureBounds.height() > 0) {
                with(LocalDensity.current) {
                    val screenWidth = LocalConfiguration.current.screenWidthDp

                    rightPanelWidth = screenWidth.dp - navigationType.featureBounds.right.toDp()
                }
            }

            if (navigationType.contentType == ContentType.LIST_AND_DETAIL) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    PetList(
                        pets = uiState.pets,
                        selectedPet = selectedPet,
                        modifier = Modifier.weight(1f),
                        listState = listState,
                        onPetClicked = onPetClicked,
                        onFavoritePetClicked = onFavoritePetClicked,
                    )

                    VerticalDivider()

                    val modifierDetails = if (rightPanelWidth > 0.dp) {
                        Modifier.requiredWidth(rightPanelWidth)
                    } else {
                        Modifier.weight(2f)
                    }

                    PetDetails(
                        pet = selectedPet,
                        modifier = modifierDetails,
                    )
                }
            } else {
                PetList(
                    pets = uiState.pets,
                    selectedPet = null,
                    modifier = Modifier.fillMaxSize(),
                    listState = listState,
                    onPetClicked = onPetClicked,
                    onFavoritePetClicked = onFavoritePetClicked,
                )
            }
        }
        AnimatedVisibility(visible = uiState.error != null) {
            Text(
                text = "Loading error: ${uiState.error ?: ""}",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
fun PetsListPreview() {
    KoinApplication(application = { modules(appComposePreviewModules) }) {
        val uiState = koinViewModel<PetsViewModel>().petListUISTate.collectAsStateWithLifecycle()

        PetsScreen(
            navigationType = NavigationType(
                NavControlType.BOTTOM_NAVIGATION,
                ContentType.LIST,
                Rect(),
            ),
            uiState = uiState.value,
            selectedPet = null,
            listState = rememberLazyListState(),
            onPetClicked = {},
            onFavoritePetClicked = {},
        )
    }
}