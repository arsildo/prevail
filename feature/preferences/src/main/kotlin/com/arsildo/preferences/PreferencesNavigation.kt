package com.arsildo.preferences

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.arsildo.preferences.appearance.AppearancePreferencesScreen

const val PREFERENCES_GRAPH = "preferenceGraph"

fun NavController.navigateToPreferences(navOptions: NavOptions? = null) {
    this.navigate(PREFERENCES_GRAPH, navOptions)
}

fun NavGraphBuilder.preferences(
    navController: NavController,
) {
    navigation(
        route = PREFERENCES_GRAPH,
        startDestination = Destinations.PREFERENCES_ROUTE,
    ) {
        composable(route = Destinations.PREFERENCES_ROUTE) {
            PreferencesScreen(
                onGeneralClick = {},
                onAppearanceClick = { navController.navigate(Destinations.APPEARANCE_PREFERENCES_ROUTE) },
                onBackClick = navController::navigateUp
            )
        }
        composable(route = Destinations.GENERAL_PREFERENCES_ROUTE) {

        }
        composable(route = Destinations.APPEARANCE_PREFERENCES_ROUTE) {
            AppearancePreferencesScreen(
                onBackClick = navController::navigateUp
            )
        }
        composable(route = Destinations.PLAYER_PREFERENCES_ROUTE) {

        }
    }
}