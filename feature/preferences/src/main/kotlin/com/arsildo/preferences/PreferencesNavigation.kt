package com.arsildo.preferences

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val PREFERENCES_GRAPH = "preferences"

fun NavController.navigateToPreferences(navOptions: NavOptions? = null) {
    this.navigate(PREFERENCES_GRAPH, navOptions)
}

fun NavGraphBuilder.preferences() {
    navigation(
        route = PREFERENCES_GRAPH,
        startDestination = Destinations.PREFERENCES_ROUTE,
    ) {
        composable(route = Destinations.PREFERENCES_ROUTE) {
            PreferencesScreen(
                onGeneralClick = {}
            )
        }
        composable(route = Destinations.GENERAL_PREFERENCES_ROUTE) {

        }
        composable(route = Destinations.PLAYER_PREFERENCES_ROUTE) {

        }
    }
}