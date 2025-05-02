package com.packt.pets.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.packt.pets.viewmodel.PetsViewModel
import com.packt.pets.views.FavoritePetsScreen
import com.packt.pets.views.PetDetailsScreen
import com.packt.pets.views.PetsScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Tom Buczynski on 01.01.2025.
 */

@Composable
fun NavigationContent(
    navigationType: NavigationType,
    navController: NavHostController,
    listState: LazyListState,
    favoriteListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val petsViewModel: PetsViewModel = koinViewModel()
    val favoritePets by petsViewModel.favoritePetList.collectAsStateWithLifecycle()
    val uiState by petsViewModel.petListUISTate.collectAsStateWithLifecycle()
    // var selectedPet by remember { mutableStateOf(uiState.pets.first()) }
    val selectedPet by petsViewModel.selectedPet.collectAsStateWithLifecycle()

    // for LeakCanary tests
    // if (petsViewModel.context == null)
    //    petsViewModel.context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Route.Pets,
        modifier = modifier,
    ) {
        composable<Route.Pets> {
            PetsScreen(
                navigationType = navigationType,
                uiState = uiState,
                selectedPet = selectedPet,
                onPetClicked = {
                    petsViewModel.setSelectedPet(it)

                    if (navigationType.contentType == ContentType.LIST) {
                        navController.navigate(Route.PetDetails(it))
                    }
                },
                listState = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                petsViewModel.updatePet(it)
            }
        }

        composable<Route.FavoritePets> {
            FavoritePetsScreen(
                favoritePets = favoritePets,
                listState = favoriteListState,
                modifier = Modifier.fillMaxSize(),
            ) {
                petsViewModel.updatePet(it.copy(isFavorite = false))
            }
        }

        composable<Route.PetDetails> { entry ->
            val route: Route.PetDetails = entry.toRoute()
            PetDetailsScreen(
                pet = route.pet,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}