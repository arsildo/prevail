package com.arsildo.prevail.logic.navigation

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
import com.arsildo.prevail.logic.viewModels.BoardListViewModel
import com.arsildo.prevail.logic.viewModels.PostListViewModel
import com.arsildo.prevail.logic.viewModels.ThreadListViewModel
import com.arsildo.prevail.presentation.screens.content.BoardListScreen
import com.arsildo.prevail.presentation.screens.content.PostListScreen
import com.arsildo.prevail.presentation.screens.content.ThreadListScreen
import com.arsildo.prevail.presentation.screens.preferences.PreferenceDetailScreen
import com.arsildo.prevail.presentation.screens.preferences.PreferenceListScreen


const val ROOT_GRAPH_ROUTE = "root"
const val CONTENT_GRAPH_ROUTE = "content"
const val PREFERENCES_GRAPH_ROUTE = "preferences"

sealed class ContentRoute(val route: String) {
    object ThreadList : ContentRoute(route = "threadList")
    object ThreadPosts : ContentRoute(route = "threadPosts")
    object BoardList : ContentRoute(route = "boardList")
}

sealed class PreferencesRoute(val route: String) {
    object PreferenceList : ContentRoute(route = "preferenceList")
    object PreferenceDetails : ContentRoute(route = "preferenceDetails")
}


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        startDestination = CONTENT_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {

        contentNavigationGraph(navController)
        preferencesNavigationGraph(navController)

    }
}


fun NavGraphBuilder.contentNavigationGraph(navController: NavHostController) {
    navigation(
        startDestination = ContentRoute.ThreadList.route,
        route = CONTENT_GRAPH_ROUTE
    ) {

        // Thread List
        composable(route = ContentRoute.ThreadList.route) {
            val threadListViewModel = hiltViewModel<ThreadListViewModel>()
            ThreadListScreen(
                navController = navController,
                viewModel = threadListViewModel,
                onThreadClicked = { threadNumber, threadName ->
                    val destination = ContentRoute.ThreadPosts.route + "/$threadNumber/$threadName"
                    navController.navigate(destination)
                }
            )
        }

        // Board List
        composable(route = ContentRoute.BoardList.route) {
            val boardListViewModel = hiltViewModel<BoardListViewModel>()
            BoardListScreen(
                navController = navController,
                viewModel = boardListViewModel
            )
        }


        // Thread Posts
        composable(
            route = ContentRoute.ThreadPosts.route + "/{threadNumber}" + "/{threadName}",
            arguments = listOf(
                navArgument("threadNumber") { type = NavType.IntType },
                navArgument("threadName") { type = NavType.StringType }
            )
        ) {
            val postListViewModel = hiltViewModel<PostListViewModel>()
            PostListScreen(
                navController = navController,
                viewModel = postListViewModel,
            )
        }
    }
}

fun NavGraphBuilder.preferencesNavigationGraph(navController: NavHostController) {
    navigation(
        startDestination = PreferencesRoute.PreferenceList.route,
        route = PREFERENCES_GRAPH_ROUTE
    ) {

        // Preference List
        composable(route = PreferencesRoute.PreferenceList.route) {
            PreferenceListScreen(
                navController = navController,
                onPreferenceCategoryClicked = { category ->
                    val destination = PreferencesRoute.PreferenceDetails.route + "/$category"
                    navController.navigate(route = destination)
                }
            )
        }

        // Preference Details
        composable(
            route = PreferencesRoute.PreferenceDetails.route + "/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            PreferenceDetailScreen(
                navController = navController,
                category = backStackEntry.arguments?.getString("category")
            )

        }

    }
}
