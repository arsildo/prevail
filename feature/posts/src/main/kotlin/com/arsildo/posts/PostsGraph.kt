package com.arsildo.posts

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

internal const val THREAD_NUMBER_ARG = "threadNumber"
internal const val POSTS_GRAPH = "posts"

fun NavController.navigateToPosts(threadNumber: Int, navOptions: NavOptions? = null) {
    this.navigate("$POSTS_GRAPH/$threadNumber", navOptions)
}


fun NavGraphBuilder.posts(
    navController: NavHostController
) {
    composable(
        route = "$POSTS_GRAPH/{$THREAD_NUMBER_ARG}",
        arguments = listOf(navArgument(THREAD_NUMBER_ARG) { type = NavType.IntType }),
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
    ) {
        PostsScreen(
            onBackPress = navController::navigateUp
        )
    }
}