package com.arsildo.prevail

import androidx.navigation.NavHostController
import com.arsildo.prevail.ContentScreens.APPEARANCE_PREFERENCES
import com.arsildo.prevail.ContentScreens.BOARDS_SCREEN
import com.arsildo.prevail.ContentScreens.GENERALS_SCREEN
import com.arsildo.prevail.ContentScreens.PLAYER_PREFERENCES
import com.arsildo.prevail.ContentScreens.POSTS_SCREEN
import com.arsildo.prevail.ContentScreens.PREFERENCES_SCREEN
import com.arsildo.prevail.ContentScreens.THREADS_SCREEN
import com.arsildo.prevail.PrevailDestinationsArg.THREAD_NUMBER_ARG

const val ROOT_GRAPH_ROUTE = "root"
const val CONTENT_GRAPH_ROUTE = "content"
const val PREFERENCES_GRAPH_ROUTE = "userPreferences"


// Routes
object ContentScreens {
    const val THREADS_SCREEN = "threads"
    const val POSTS_SCREEN = "posts"
    const val BOARDS_SCREEN = "boards"

    const val PREFERENCES_SCREEN = "preferences"
    const val GENERALS_SCREEN = "generalPreferences"
    const val APPEARANCE_PREFERENCES = "appearancesPreferences"
    const val PLAYER_PREFERENCES = "playerPreferences"
}

// Arguments
object PrevailDestinationsArg {
    const val THREAD_NUMBER_ARG = "threadNumber"
}

// Destinations
object PrevailDestinations {
    const val THREADS_ROUTE = THREADS_SCREEN
    const val POSTS_ROUTE = "$POSTS_SCREEN/{$THREAD_NUMBER_ARG}"
    const val BOARDS_ROUTE = BOARDS_SCREEN

    const val PREFERENCES_ROUTE = PREFERENCES_SCREEN
    const val GENERALS_ROUTE = GENERALS_SCREEN
    const val APPEARANCE_PREFS_ROUTE = APPEARANCE_PREFERENCES
    const val PLAYER_PREFS_ROUTE = PLAYER_PREFERENCES
}


class PrevailNavigationActions(private val navController: NavHostController) {

    fun navigateToPosts(threadNumber: Int) {
        navController.navigate("$POSTS_SCREEN/$threadNumber")
    }

}