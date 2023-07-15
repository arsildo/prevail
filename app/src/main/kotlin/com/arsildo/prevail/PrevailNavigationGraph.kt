package com.arsildo.prevail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.preferences.navigateToPreferences
import com.arsildo.preferences.preferences
import com.arsildo.threadcatalog.ThreadsScreen

@Composable
fun PrevailNavigationGraph(
    navController: NavHostController = rememberNavController()
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
                onThreadClick = {
                    navController.navigateToPreferences()
                }
            )
        }
        preferences()
    }
}