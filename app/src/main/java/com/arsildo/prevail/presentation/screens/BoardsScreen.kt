package com.arsildo.prevail.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arsildo.prevail.logic.viewmodels.BoardsScreenState
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.presentation.components.shared.ScreenLayout

@Composable
fun BoardsScreen(navController: NavController) {

    val boardViewModel = hiltViewModel<BoardsViewModel>()

    when (boardViewModel.boardsScreenState.value) {
        is BoardsScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            )
            { Text(text = "Loading") }
        }

        is BoardsScreenState.Failed -> {
            Text(text = "Failed", color = MaterialTheme.colorScheme.primary)
        }

        is BoardsScreenState.Responded -> {
            ScreenLayout {
                LazyColumn {
                    boardViewModel.boardList.value?.boards?.let { it ->
                        items(it.size) {
                            Text(
                                text = boardViewModel.boardList.value!!.boards[it].board,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = boardViewModel.boardList.value!!.boards[it].title,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = boardViewModel.boardList.value!!.boards[it].meta_description,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}