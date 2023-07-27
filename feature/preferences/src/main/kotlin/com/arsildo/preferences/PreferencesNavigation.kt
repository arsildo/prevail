package com.arsildo.preferences

import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.arsildo.preferences.appearance.AppearancePreferencesScreen
import com.arsildo.prevail.MainActivityViewModel
import org.koin.androidx.compose.koinViewModel

private const val PREFERENCES_GRAPH = "preferenceGraph"

fun NavController.navigateToPreferences(navOptions: NavOptions? = null) {
    this.navigate(
        route = PREFERENCES_GRAPH,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.preferences(
    navController: NavHostController,
) {
    navigation(
        route = PREFERENCES_GRAPH,
        startDestination = Destinations.PREFERENCES_ROUTE,
    ) {

        composable(route = Destinations.PREFERENCES_ROUTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(PREFERENCES_GRAPH)
            }
            val viewModel = koinViewModel<MainActivityViewModel>(viewModelStoreOwner = parentEntry)
            PreferencesScreen(
                onGeneralClick = { viewModel.updateTestValue() },
                onAppearanceClick = { navController.navigate(Destinations.APPEARANCE_PREFERENCES_ROUTE) },
                onBackClick = navController::navigateUp
            )
        }
        composable(route = Destinations.GENERAL_PREFERENCES_ROUTE) {
            Text(text = "1")
        }
        composable(route = Destinations.APPEARANCE_PREFERENCES_ROUTE) {
            AppearancePreferencesScreen(
                onBackClick = navController::navigateUp
            )
        }
        composable(route = Destinations.PLAYER_PREFERENCES_ROUTE) {
            Text(text = "1")
        }
    }
}