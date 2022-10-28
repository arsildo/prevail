package com.arsildo.prevail.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.arsildo.prevail.logic.navigation.Destinations
import com.arsildo.prevail.presentation.components.ScreenLayout

@Composable
fun PreferencesScreen(navController: NavController) {
    ScreenLayout {
        Text(text = "Boards")
        Text(text = "Themes")
        Text(text = "Posts")
        Button(onClick = { navController.navigate(Destinations.Main.route) }) {
            Text(text = "Main")
        }
    }
}