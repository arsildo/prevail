package com.arsildo.prevail

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.arsildo.prevail.preferences.AppearancesViewModel
import com.arsildo.prevail.theme.PrevailTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val viewModel = hiltViewModel<AppearancesViewModel>()
            val systemColorScheme by viewModel.getSystemColorScheme().collectAsState(true)
            val colorScheme by viewModel.getColorScheme().collectAsState(true)
            val dynamicColorScheme by viewModel.getDynamicColorScheme().collectAsState(true)

            PrevailTheme(
                darkTheme = if (systemColorScheme) isSystemInDarkTheme() else colorScheme,
                dynamicColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicColorScheme else false
            ) {
                val navController = rememberNavController()
                PrevailNavigationGraph(navController = navController)
            }
        }


    }
}