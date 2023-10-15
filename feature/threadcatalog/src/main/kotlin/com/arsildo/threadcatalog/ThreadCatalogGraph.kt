package com.arsildo.threadcatalog

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val THREAD_CATALOG_GRAPH = "threadCatalogGraph"

fun NavController.navigateToThreadCatalog() {
    this.navigate(route = THREAD_CATALOG_GRAPH) {
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.threadCatalog(
    navController: NavController,
    onThreadClick: (Int) -> Unit,
    onBoardsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
) {
    navigation(
        route = THREAD_CATALOG_GRAPH,
        startDestination = "threadCatalog"
    ) {
        composable(
            route = "threadCatalog",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) }
        ) {
            ThreadsScreen(
                onThreadClick = onThreadClick,
                onBoardsClick = onBoardsClick,
                onPreferencesClick = onPreferencesClick
            )
        }
    }
}