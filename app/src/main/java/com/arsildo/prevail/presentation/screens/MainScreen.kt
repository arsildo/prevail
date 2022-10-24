package com.arsildo.prevail.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.logic.viewmodels.ScreenState
import com.arsildo.prevail.presentation.components.ScreenLayout

@Composable
fun MainScreen() {

    val viewModel = hiltViewModel<BoardsViewModel>()

    ScreenLayout {
        when (viewModel.screenState.value) {
            is ScreenState.Loading -> {
                CircularProgressIndicator()
                Text(text = "Loading", color = MaterialTheme.colorScheme.primary)
            }

            is ScreenState.Failed -> {
                Text(text = "Failed", color = MaterialTheme.colorScheme.primary)
            }

            is ScreenState.Responded -> {
                Text(text = "Response", color = MaterialTheme.colorScheme.primary)
                LazyColumn {
                    viewModel.boardList.value?.boards?.let { it ->
                        items(it.size) {
                            Text(
                                text = viewModel.boardList.value!!.boards[it].title,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}