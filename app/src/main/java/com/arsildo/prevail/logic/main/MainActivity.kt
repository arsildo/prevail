package com.arsildo.prevail.logic.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import com.arsildo.prevail.logic.cache.CachedPreferences
import com.arsildo.prevail.logic.navigation.Destinations
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.presentation.screens.MainScreen
import com.arsildo.prevail.presentation.screens.PreferencesScreen
import com.arsildo.prevail.presentation.theme.PrevailTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )




        setContent {
            val dataStore = CachedPreferences(LocalContext.current)
            val dynamicThemeEnabled = dataStore.getDynamicColorSchemePreference.collectAsState(
                initial = true
            ).value
            PrevailTheme(
                dynamicColor = dynamicThemeEnabled
            ) {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }


    @Composable
    fun NavigationGraph(navController: NavHostController) {
        val viewModel = hiltViewModel<BoardsViewModel>()
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            composable(route = Destinations.Main.route) {
                MainScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(route = Destinations.Preferences.route) {
                PreferencesScreen(navController)
            }
        }
    }

}