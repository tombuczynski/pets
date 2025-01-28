package com.packt.pets.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Created by Tom Buczynski on 02.01.2025.
 */

@Composable
fun NavRail(
    currentRoute: String?,
    onMenuIconClicked: () -> Unit,
    onNavButtonClicked: (Route) -> Unit
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        NavigationRailItem(
            selected = false,
            onClick = onMenuIconClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        )

        NavigationRailItem(
            selected = isRouteCurrent(Route.Pets::class, currentRoute),
            onClick = { onNavButtonClicked(Route.Pets) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "All Pets (Home)"
                )
            }
        )

        NavigationRailItem(
            selected = isRouteCurrent(Route.FavoritePets::class, currentRoute),
            onClick = { onNavButtonClicked(Route.FavoritePets) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite Pets"
                )
            }
        )
    }
}