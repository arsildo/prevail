package com.arsildo.prevail.media

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.arsildo.prevail.threads.LocalBoardContext
import com.arsildo.prevail.utils.LocalMediaAspectRatio
import com.arsildo.prevail.utils.MediaPlayer

@Composable
fun MediaScreen(
    navController: NavHostController,
    viewModel: MediaViewModel
) {
    CompositionLocalProvider(LocalBoardContext provides viewModel.currentBoard.value) {
        CompositionLocalProvider(LocalMediaAspectRatio provides viewModel.aspectRatio) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = navController::popBackStack),
                contentAlignment = Alignment.Center
            ) {

                MediaPlayer(
                    mediaID = viewModel.mediaID,
                    inFocus = true,
                    playerRepository = viewModel.playerRepository
                )

                IconButton(
                    onClick = navController::popBackStack,
                    modifier = Modifier.align(Alignment.TopStart),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
    BackHandler {
        navController.popBackStack()
    }
}