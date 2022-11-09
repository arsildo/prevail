package com.arsildo.prevail.presentation.screens


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.arsildo.prevail.presentation.components.shared.LoadingResponse

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BoardsScreen(navController: NavController) {
    val boardViewModel = hiltViewModel<BoardsViewModel>()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            val statusBarPadding by animateDpAsState(
                if (topAppBarState.collapsedFraction < .99)
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding() else 0.dp,
                animationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
            )
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Destinations.Main.route) }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = "boards",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Rounded.SelectAll,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                    .padding(top = statusBarPadding)

            )

        },
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 8.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            when (boardViewModel.boardsScreenState.value) {
                is BoardsScreenState.Loading -> LoadingResponse(text = "Loading boards...")
                is BoardsScreenState.Failed -> LoadingResponse(text = "Failed to load data.\n Please check your internet connection.")

                is BoardsScreenState.Responded -> {
                    val listState = rememberLazyListState()
                    Column {
                        SearchBoard(topAppBarState = topAppBarState)
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
    }
}