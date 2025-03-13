package com.packt.pets.navigation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.packt.pets.viewmodel.PetsViewModel
import com.packt.pets.views.FavoritePetsScreen
import com.packt.pets.views.PermissionDialog
import com.packt.pets.views.PermissionStatus
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
    var permissionStatus by remember { mutableStateOf(PermissionStatus.UNKNOWN) }
    val context = LocalContext.current

    val petsViewModel: PetsViewModel = koinViewModel()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionDialog(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            onPermissionAction = {
                if (it == PermissionStatus.GRANTED) {
                    permissionStatus = it
                } else if (it == PermissionStatus.DENIED) {
                    permissionStatus = it

                    Toast.makeText(
                        context,
                        "Location permission denied, content can't be shown",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            },
        )
    } else {
        permissionStatus = PermissionStatus.GRANTED
    }

    NavHost(
        navController = navController,
        startDestination = Route.Pets,
        modifier = modifier,
    ) {
        composable<Route.Pets> {
            if (permissionStatus == PermissionStatus.GRANTED) {
                PetsScreen(
                    navigationType = navigationType,
                    listState = listState,
                    petsViewModel = petsViewModel,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    navController.navigate(Route.PetDetails(it))
                }
            } else {
                Box(modifier = Modifier.fillMaxSize())
            }
        }

        composable<Route.FavoritePets> {
            if (permissionStatus == PermissionStatus.GRANTED) {
                FavoritePetsScreen(
                    listState = favoriteListState,
                    petsViewModel = petsViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Box(modifier = Modifier.fillMaxSize())
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