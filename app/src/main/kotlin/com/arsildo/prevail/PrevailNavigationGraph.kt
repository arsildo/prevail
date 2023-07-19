package com.arsildo.prevail

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.preferences.navigateToPreferences
import com.arsildo.preferences.preferences
import com.arsildo.threadcatalog.ThreadsScreen

@Composable
fun PrevailNavigationGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainActivityViewModel,
) {
    NavHost(
        navController = navController,
        route = ROOT_GRAPH,
        startDestination = Destinations.THREADS_ROUTE
    ) {
        composable(
            route = Destinations.THREADS_ROUTE
        ) {
            ThreadsScreen(
                onThreadClick = { viewModel.updateTestValue() },
                onPreferencesClick = navController::navigateToPreferences,
                testValue = viewModel.testValue
            )
        }
        preferences(
            navController = navController,
            testValue = viewModel.testValue,
            updateTestValue = viewModel::updateTestValue
        )
    }
}