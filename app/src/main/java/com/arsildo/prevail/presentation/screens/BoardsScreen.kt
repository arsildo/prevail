package com.arsildo.prevail.presentation.screens


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arsildo.prevail.logic.Destinations
import com.arsildo.prevail.logic.viewmodels.BoardsScreenState
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.presentation.components.boards.BoardCard
import com.arsildo.prevail.presentation.components.boards.SearchBoard
import com.arsildo.prevail.presentation.components.shared.CollapsingTopAppBar
import com.arsildo.prevail.presentation.components.shared.LoadingResponse
import com.arsildo.prevail.presentation.components.shared.ScreenLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(navController: NavController) {
    val boardViewModel = hiltViewModel<BoardsViewModel>()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearEasing)
    )

    ScreenLayout(
        topBar = {
            CollapsingTopAppBar(
                appBarState = topBarState,
                scrollBehavior = scrollBehavior,
                title = {
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Boards",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Rounded.SelectAll,
                            contentDescription = null
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Destinations.Main.route) }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) {
        when (boardViewModel.boardsScreenState.value) {
            is BoardsScreenState.Loading -> {
                LoadingResponse(text = "Loading boards...")
            }

            is BoardsScreenState.Failed -> {
                LoadingResponse(text = "Failed to load data.\n Please check your internet connection.")
            }

            is BoardsScreenState.Responded -> {
                SearchBoard(appBarState = topBarState)
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                ) {
                    items(boardViewModel.boardList.value.boards.size) {
                        BoardCard(
                            title = boardViewModel.boardList.value.boards[it].board,
                            desc = boardViewModel.boardList.value.boards[it].title,
                            fullDesc = boardViewModel.boardList.value.boards[it].meta_description
                        )
                    }
                }
            }
        }
    }
}