package com.arsildo.prevail

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.arsildo.posts.navigateToPosts
import com.arsildo.posts.posts
import com.arsildo.preferences.navigateToPreferences
import com.arsildo.preferences.preferences
import com.arsildo.threadcatalog.THREAD_CATALOG_GRAPH
import com.arsildo.threadcatalog.threadCatalog

@Composable
fun PrevailNavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = THREAD_CATALOG_GRAPH
    ) {
        threadCatalog(
            navController = navController,
            onThreadClick = navController::navigateToPosts,
            onPreferencesClick = navController::navigateToPreferences,
        )
        posts(
            navController = navController
        )
        preferences(
            navController = navController
        )
    }
}
