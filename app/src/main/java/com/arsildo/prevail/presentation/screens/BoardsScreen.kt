package com.arsildo.prevail.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.Alignment
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
import com.arsildo.prevail.presentation.components.shared.ScreenLayout

@OptIn(ExperimentalMaterial3Api::class)
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
                val listState = rememberLazyListState()
                val appBarState = rememberTopAppBarState()
                val scrollBehavior =
                    TopAppBarDefaults.enterAlwaysScrollBehavior(
                        state = appBarState,
                        snapAnimationSpec = tween(
                            delayMillis = 0,
                            durationMillis = 256,
                            easing = LinearOutSlowInEasing,
                        )

                    )

                CollapsingTopAppBar(
                    appBarState = appBarState,
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
                WindowInsets.statusBars
                SearchBoard(appBarState = appBarState)
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .animateContentSize(
                            tween(
                                delayMillis = 0,
                                durationMillis = 256,
                                easing = LinearOutSlowInEasing,
                            )
                        )
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .padding(horizontal = 8.dp),
                ) {
                    boardViewModel.boardList.value?.boards?.let { it ->
                        items(it.size) {
                            BoardCard(
                                title = boardViewModel.boardList.value!!.boards[it].board,
                                desc = boardViewModel.boardList.value!!.boards[it].title,
                                fullDecs = boardViewModel.boardList.value!!.boards[it].meta_description
                            )
                        }
                    }
                }
            }
        }
    }
}