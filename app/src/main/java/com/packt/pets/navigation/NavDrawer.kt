package com.packt.pets.navigation

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * Created by Tom Buczynski on 02.01.2025.
 */

@Composable
fun NavDrawer(
    currentRoute: String?,
    onNavButtonClicked: (Route) -> Unit,
    drawerState: DrawerState,
    onCloseGesture: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        gesturesEnabled = onCloseGesture == null,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surfaceContainer) {
                NavDrawerContent(currentRoute, onNavButtonClicked)
            }
        },
        content = content,
        drawerState = drawerState,
        modifier = Modifier
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (onCloseGesture != null && (dragAmount < -3f) &&
                        drawerState.isOpen && !drawerState.isAnimationRunning
                    ) {
                        onCloseGesture()
                    }
                }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        if (onCloseGesture != null &&
                            event.type == PointerEventType.Release &&
                            event.changes.any { it.isConsumed } &&
                            drawerState.isOpen && !drawerState.isAnimationRunning
                        ) {
                            onCloseGesture()
                        }

                        // Log.d("NavDrawer", "${event.type}, ${event.changes.first().isConsumed}")
                    }
                }
            },
    )
}

@Composable
fun NavDrawerContent(
    currentRoute: String?,
    onNavButoonClicked: (Route) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp),
    ) {
        Text(
            text = "Pets",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(8.dp),
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        NavigationDrawerItem(
            selected = isRouteCurrent(Route.Pets::class, currentRoute),
            onClick = { onNavButoonClicked(Route.Pets) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "All Pets (Home)",
                )
            },
            label = {
                Text(text = "All Pets")
            },
        )

        NavigationDrawerItem(
            selected = isRouteCurrent(Route.FavoritePets::class, currentRoute),
            onClick = { onNavButoonClicked(Route.FavoritePets) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite Pets",
                )
            },
            label = {
                Text(text = "Favorities")
            },
        )
    }
}