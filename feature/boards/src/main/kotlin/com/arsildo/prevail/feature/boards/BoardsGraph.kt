package com.arsildo.prevail.feature.boards

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

internal const val BOARDS_GRAPH = "boardsGraph"
fun NavController.navigateToBoards() {
    this.navigate(BOARDS_GRAPH)
}

fun NavGraphBuilder.boards(
    navController: NavHostController
) {
    navigation(
        startDestination = "boards",
        route = BOARDS_GRAPH
    ) {
        composable(
            route = "boards",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) }
        ) {
            BoardsScreen(
                onBackPress = navController::navigateUp
            )
        }
    }
}