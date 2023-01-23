package com.arsildo.prevail

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.arsildo.prevail.PrevailDestinations.APPEARANCE_PREFS_ROUTE
import com.arsildo.prevail.PrevailDestinations.MEDIA_ROUTE
import com.arsildo.prevail.PrevailDestinations.PLAYER_PREFS_ROUTE
import com.arsildo.prevail.PrevailDestinations.POSTS_ROUTE
import com.arsildo.prevail.PrevailDestinations.PREFERENCES_ROUTE
import com.arsildo.prevail.PrevailDestinations.THREADS_ROUTE
import com.arsildo.prevail.PrevailDestinationsArg.MEDIA_ASPECT_RATIO_ARG
import com.arsildo.prevail.PrevailDestinationsArg.MEDIA_ID_ARG
import com.arsildo.prevail.PrevailDestinationsArg.THREAD_NUMBER_ARG
import com.arsildo.prevail.boards.BoardsScreen
import com.arsildo.prevail.boards.BoardsViewModel
import com.arsildo.prevail.media.MediaScreen
import com.arsildo.prevail.media.MediaViewModel
import com.arsildo.prevail.posts.PostsScreen
import com.arsildo.prevail.posts.PostsViewModel
import com.arsildo.prevail.preferences.PreferencesScreen
import com.arsildo.prevail.preferences.appearances.AppearancesPreferencesScreen
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

@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.contentNavigationGraph(
    navController: NavHostController,
    threadsListViewModel: ThreadsViewModel
) {
    navigation(
        startDestination = THREADS_ROUTE,
        route = CONTENT_GRAPH_ROUTE
    ) {

        // Thread List
        composable(route = THREADS_ROUTE) {
            ThreadsScreen(
                navController = navController,
                viewModel = threadsListViewModel,
                onThreadClicked = { threadNumber ->
                    PrevailNavigationActions(navController).navigateToPosts(threadNumber)
                }
            )
        }

        // Thread Posts
        composable(
            route = POSTS_ROUTE,
            arguments = listOf(navArgument(THREAD_NUMBER_ARG) { type = NavType.IntType })
        ) {
            val postsViewModel = hiltViewModel<PostsViewModel>()
            PostsScreen(navController = navController, viewModel = postsViewModel)
        }

        dialog(
            route = MEDIA_ROUTE,
            arguments = listOf(
                navArgument(MEDIA_ID_ARG) { type = NavType.LongType },
                navArgument(MEDIA_ASPECT_RATIO_ARG) { type = NavType.FloatType }
            ),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val mediaViewModel = hiltViewModel<MediaViewModel>()
            MediaScreen(navController = navController, viewModel = mediaViewModel)
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
        startDestination = PREFERENCES_ROUTE,
        route = PREFERENCES_GRAPH_ROUTE
    ) {

        // Preference List
        composable(route = PREFERENCES_ROUTE) {
            PreferencesScreen(navController = navController)
        }

        // Appearance Preferences
        composable(route = APPEARANCE_PREFS_ROUTE) {
            AppearancesPreferencesScreen(navController = navController)
        }
        // Player Preferences
        composable(route = PLAYER_PREFS_ROUTE) {
            PlayerPreferencesScreen(navController = navController)
        }

    }
}
