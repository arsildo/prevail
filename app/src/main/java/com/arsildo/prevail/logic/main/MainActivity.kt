package com.arsildo.prevail.logic.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arsildo.prevail.logic.navigation.Destinations
import com.arsildo.prevail.presentation.screens.MainScreen
import com.arsildo.prevail.presentation.theme.PrevailTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrevailTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }


    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            composable(route = Destinations.Main.route) {
                MainScreen()
            }
        }
    }

}