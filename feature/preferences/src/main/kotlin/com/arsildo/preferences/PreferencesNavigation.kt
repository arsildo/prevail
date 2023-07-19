package com.arsildo.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.arsildo.preferences.appearance.AppearancePreferencesScreen

const val PREFERENCES_GRAPH = "preferenceGraph"

fun NavController.navigateToPreferences(navOptions: NavOptions? = null) {
    this.navigate(route = PREFERENCES_GRAPH, navOptions)
}

fun NavGraphBuilder.preferences(
    navController: NavController,
    testValue: Boolean,
    updateTestValue: () -> Unit,
) {
    navigation(
        route = PREFERENCES_GRAPH,
        startDestination = Destinations.PREFERENCES_ROUTE,
    ) {
        composable(route = Destinations.PREFERENCES_ROUTE) {
            PreferencesScreen(
                onGeneralClick = {},
                onAppearanceClick = { navController.navigate(Destinations.GENERAL_PREFERENCES_ROUTE) },
                onBackClick = navController::navigateUp
            )
        }
        composable(route = Destinations.GENERAL_PREFERENCES_ROUTE) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Button(onClick = updateTestValue) {
                        Text(text = "Update")
                    }
                    Text(text = testValue.toString())
                }
            }
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