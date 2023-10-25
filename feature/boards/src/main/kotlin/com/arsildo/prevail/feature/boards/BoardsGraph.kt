package com.arsildo.prevail.feature.boards

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

internal const val BOARDS_GRAPH = "boardsGraph"
fun NavController.navigateToBoards() {
    this.navigate(route = BOARDS_GRAPH){
        launchSingleTop = true
    }
}

fun NavGraphBuilder.boards(
    navController: NavHostController,
    startDestination : String = "boards"
) {
    navigation(
        route = BOARDS_GRAPH,
        startDestination = startDestination
    ) {
        composable(
            route = startDestination,
            enterTransition = { slideIntoContainer(SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(SlideDirection.Right) }
        ) {
            BoardsScreen(
                onBackPress = navController::navigateUp
            )
        }
    }
}