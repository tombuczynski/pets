package com.packt.pets.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.adaptive.collectFoldingFeaturesAsState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.FoldingFeature
import com.packt.pets.views.MainScreen
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Composable
fun AppNavigation() {
    val windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val foldingFeature: FoldingFeature? = collectFoldingFeaturesAsState().value.firstOrNull()

    val navigationType = determineNavigationType(windowSizeClass, foldingFeature)

    val navController = rememberNavController()

    val listState = rememberLazyListState()
    val favoriteListState = rememberLazyListState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // val currentDestination = navBackStackEntry?.destination
    // selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val currentRoute: String? = navBackStackEntry?.destination?.route

    if (navigationType.contentType == ContentType.LIST_AND_DETAIL &&
        isRouteCurrent(Route.PetDetails::class, currentRoute)
    ) {
        navController.popBackStack()
    }

    val navigation = { route: Route ->
        navController.navigate(route) {
            // Avoid multiple copies of the same destination when
            // reselecting the same item
//            launchSingleTop = true

            // back operation always closes the Activity
//            navController.popBackStack()

            popUpTo(navController.graph.findStartDestination().id) {
//                inclusive = true
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true

            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuDrawer: (open: Boolean) -> Unit = {
        scope.launch {
            with(drawerState) {
                if (it) open() else close()
            }
        }
    }

    if (navigationType.navControlType == NavControlType.BOTTOM_NAVIGATION) {
        MainScreen(
            navigationType = navigationType,
            currentRoute = currentRoute,
            onMenuIconClicked = { menuDrawer(true) },
            onNavButtonClicked = navigation,
            listState = listState,
            favoriteListState = favoriteListState,
            navController = navController,
        )
    } else {
        NavDrawer(
            currentRoute = currentRoute,
            drawerState = drawerState,
            onNavButtonClicked = {
                navigation(it)
                // menuDrawer(false)
            },
            onCloseGesture = { menuDrawer(false) },
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                if (navigationType.navControlType == NavControlType.NAVIGATION_RAIL) {
                    NavRail(
                        currentRoute = currentRoute,
                        onMenuIconClicked = { menuDrawer(true) },
                        onNavButtonClicked = navigation,
                    )
                }

                MainScreen(
                    navigationType = navigationType,
                    currentRoute = currentRoute,
                    onMenuIconClicked = { menuDrawer(true) },
                    onNavButtonClicked = navigation,
                    listState = listState,
                    favoriteListState = favoriteListState,
                    navController = navController,
                )
            }
        }
    }
}

fun <T : Route> isRouteCurrent(routeClass: KClass<T>, currentRoute: String?): Boolean {
    // Log.i("isRouteCurrent", "currentRoute: $currentRoute, routeClass: ${routeClass.qualifiedName} ")
    return routeClass.qualifiedName?.let { currentRoute?.contains(it) } == true
}