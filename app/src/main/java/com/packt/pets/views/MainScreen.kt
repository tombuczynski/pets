package com.packt.pets.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.packt.pets.navigation.BottomNavBar
import com.packt.pets.navigation.ContentType
import com.packt.pets.navigation.NavigationContent
import com.packt.pets.navigation.NavControlType
import com.packt.pets.navigation.NavigationType
import com.packt.pets.navigation.Route
import com.packt.pets.navigation.isRouteCurrent

/**
 * Created by Tom Buczynski on 28.12.2024.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigationType: NavigationType,
    currentRoute: String?,
    onMenuIconClicked: () -> Unit,
    onNavButtonClicked: (Route) -> Unit,
    listState: LazyListState,
    favoriteListState: LazyListState,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            if (navigationType.contentType != ContentType.LIST_AND_DETAIL) {
                TopAppBar(
                    title = {
                        Text(text = "Pets")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        if (isRouteCurrent(Route.PetDetails::class, currentRoute)) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )

                            }
                        } else if (navigationType.navControlType == NavControlType.NAVIGATION_DRAWER) {
                            IconButton(onClick = onMenuIconClicked) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                )
            }
        },
        content =  {
            NavigationContent(
                navigationType = navigationType,
                navController = navController,
                listState = listState,
                modifier = Modifier.fillMaxSize().padding(it),
                favoriteListState = favoriteListState
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = navigationType.navControlType == NavControlType.BOTTOM_NAVIGATION
                        && !isRouteCurrent(Route.PetDetails::class, currentRoute)
            ) {
                BottomNavBar(currentRoute, onNavButtonClicked)
            }
        }
    )
}

