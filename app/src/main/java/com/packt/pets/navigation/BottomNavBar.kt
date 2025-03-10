package com.packt.pets.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Created by Tom Buczynski on 02.01.2025.
 */

@Composable
fun BottomNavBar(currentRoute: String?, onNavButoonClicked: (Route) -> Unit) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        NavigationBarItem(
            selected = isRouteCurrent(Route.Pets::class, currentRoute),
            onClick = { onNavButoonClicked(Route.Pets) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "All Pets (Home)",
                )
            },
            label = {
                Text(text = "All Pets")
            },
        )

        NavigationBarItem(
            selected = isRouteCurrent(Route.FavoritePets::class, currentRoute),
            onClick = { onNavButoonClicked(Route.FavoritePets) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = "Favorite Pets",
                )
            },
            label = {
                Text(text = "Favorities")
            },
        )
    }
}