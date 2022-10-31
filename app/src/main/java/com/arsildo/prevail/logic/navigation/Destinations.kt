package com.arsildo.prevail.logic.navigation

sealed class Destinations(val route: String) {
    object Main : Destinations(route = "main_screen")
    object Preferences : Destinations(route = "preferences_screen")
}