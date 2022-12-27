package com.arsildo.prevail

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.arsildo.prevail.preferences.AppearancesViewModel
import com.arsildo.prevail.theme.PrevailTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val colorSchemeViewModel = hiltViewModel<AppearancesViewModel>()
            val systemColorScheme =
                colorSchemeViewModel.getSystemColorScheme().collectAsState(initial = true).value
            val colorScheme = colorSchemeViewModel
                .getColorScheme().collectAsState(true).value
            val dynamicColorScheme =
                colorSchemeViewModel.getDynamicColorScheme().collectAsState(initial = true).value

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