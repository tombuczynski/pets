package com.packt.pets.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.packt.pets.views.FavoritePetsScreen
import com.packt.pets.views.PetDetailsScreen
import com.packt.pets.views.PetsScreen

/**
 * Created by Tom Buczynski on 01.01.2025.
 */

@Composable
fun NavigationContent(
    navigationType: NavigationType,
    navController: NavHostController,
    listState: LazyListState,
    favoriteListState: LazyListState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination =  Route.Pets,
        modifier = modifier
    ) {
        composable<Route.Pets> {
            PetsScreen(
                navigationType = navigationType,
                modifier = Modifier.fillMaxSize(),
                listState = listState
            ) {
                navController.navigate(Route.PetDetails(it))
            }
        }

        composable<Route.FavoritePets> {
            FavoritePetsScreen(
                navigationType = navigationType,
                modifier = Modifier.fillMaxSize(),
                listState = favoriteListState
            )
        }

        composable<Route.PetDetails> { entry ->
            val route: Route.PetDetails = entry.toRoute()
            PetDetailsScreen(
                pet = route.pet,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}