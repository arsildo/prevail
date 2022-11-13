package com.arsildo.prevail.logic

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.prevail.logic.cache.ColorSchemePreferences
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.logic.viewmodels.ThreadsViewModel
import com.arsildo.prevail.presentation.components.preferences.PreferenceCategory
import com.arsildo.prevail.presentation.screens.BoardsScreen
import com.arsildo.prevail.presentation.screens.MainScreen
import com.arsildo.prevail.presentation.screens.PreferenceDetailScreen
import com.arsildo.prevail.presentation.screens.PreferencesScreen
import com.arsildo.prevail.presentation.theme.PrevailTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val dataStore = ColorSchemePreferences(LocalContext.current)
            val systemColorScheme = dataStore.getSystemColorScheme.collectAsState(true).value
            val colorScheme = dataStore.getColorScheme.collectAsState(true).value
            val dynamicColorScheme = dataStore.getDynamicColorScheme.collectAsState(true).value

            PrevailTheme(
                darkTheme = if (systemColorScheme) isSystemInDarkTheme() else colorScheme,
                dynamicColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicColorScheme else false
            ) {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        val threadsViewModel = hiltViewModel<ThreadsViewModel>()
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            composable(route = Destinations.Main.route) {
                MainScreen(navController = navController, viewModel = threadsViewModel)
            }
            composable(route = Destinations.Boards.route) {
                BoardsScreen(navController)
            }

            var destination = PreferenceCategory.Appearance.route
            composable(route = Destinations.Preferences.route) {
                PreferencesScreen(navController) {
                    destination = it
                    navController.navigate(Destinations.PreferenceDetailsScreen.route)
                }
            }
            composable(route = Destinations.PreferenceDetailsScreen.route) {
                PreferenceDetailScreen(destination = destination, navController)
            }
        }
    }

}

sealed class Destinations(val route: String) {
    object Main : Destinations(route = "main_screen")
    object Boards : Destinations(route = "boards_screen")
    object Preferences : Destinations(route = "preferences_screen")
    object PreferenceDetailsScreen : Destinations(route = "preference_details_screen")
}

