package com.arsildo.prevail

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.arsildo.prevail.PrevailDestinationsArg.THREAD_NUMBER_ARG
import com.arsildo.prevail.boards.BoardsScreen
import com.arsildo.prevail.boards.BoardsViewModel
import com.arsildo.prevail.posts.PostsScreen
import com.arsildo.prevail.posts.PostsViewModel
import com.arsildo.prevail.preferences.PreferencesScreen
import com.arsildo.prevail.preferences.appearances.AppearancesPreferencesScreen
import com.arsildo.prevail.preferences.general.GeneralPreferencesScreen
import com.arsildo.prevail.preferences.player.PlayerPreferencesScreen
import com.arsildo.prevail.threads.ThreadsScreen
import com.arsildo.prevail.threads.ThreadsViewModel


@Composable
fun PrevailNavigationGraph(navController: NavHostController) {
    val threadsViewModel = hiltViewModel<ThreadsViewModel>()
    NavHost(
        navController = navController,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        startDestination = CONTENT_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {
        contentNavigationGraph(navController, threadsViewModel)
        preferencesNavigationGraph(navController)
    }
}

// PrevailDestinations >> ROUTES
// PrevailDestinationsArgs >> ARGUMENTS

fun NavGraphBuilder.contentNavigationGraph(
    navController: NavHostController,
    threadsListViewModel: ThreadsViewModel
) {
    navigation(
        startDestination = PrevailDestinations.THREADS_ROUTE,
        route = CONTENT_GRAPH_ROUTE
    ) {

        // Thread List
        composable(route = PrevailDestinations.THREADS_ROUTE) {
            ThreadsScreen(
                navController = navController,
                viewModel = threadsListViewModel,
                onThreadClicked = PrevailNavigationActions(navController)::navigateToPosts,
                onPlayableMediaClicked = {}
            )
        }

        // Thread Posts
        composable(
            route = PrevailDestinations.POSTS_ROUTE,
            arguments = listOf(navArgument(THREAD_NUMBER_ARG) { type = NavType.IntType })
        ) {
            val postsViewModel = hiltViewModel<PostsViewModel>()
            PostsScreen(navController = navController, viewModel = postsViewModel)
        }

        // Board List
        composable(route = PrevailDestinations.BOARDS_ROUTE) {
            val boardsViewModel = hiltViewModel<BoardsViewModel>()
            BoardsScreen(navController = navController, viewModel = boardsViewModel)
        }

    }
}

fun NavGraphBuilder.preferencesNavigationGraph(navController: NavHostController) {
    navigation(
        startDestination = PrevailDestinations.PREFERENCES_ROUTE,
        route = PREFERENCES_GRAPH_ROUTE
    ) {

        // Preference List
        composable(route = PrevailDestinations.PREFERENCES_ROUTE) {
            PreferencesScreen(navController = navController)
        }

        // General Preferences
        composable(route = PrevailDestinations.GENERALS_ROUTE) {
            GeneralPreferencesScreen(navController = navController)
        }

        // Appearance Preferences
        composable(route = PrevailDestinations.APPEARANCE_PREFS_ROUTE) {
            AppearancesPreferencesScreen(navController = navController)
        }
        // Player Preferences
        composable(route = PrevailDestinations.PLAYER_PREFS_ROUTE) {
            PlayerPreferencesScreen(navController = navController)
        }

    }
}
