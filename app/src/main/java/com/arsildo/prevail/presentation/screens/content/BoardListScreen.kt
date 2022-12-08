package com.arsildo.prevail.presentation.screens.content


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.navigation.ContentRoute
import com.arsildo.prevail.logic.viewModels.BoardListScreenState
import com.arsildo.prevail.logic.viewModels.BoardListViewModel
import com.arsildo.prevail.presentation.components.boardList.BoardCard
import com.arsildo.prevail.presentation.components.boardList.SearchBoard
import com.arsildo.prevail.presentation.components.shared.AppBar
import com.arsildo.prevail.presentation.components.shared.LoadingResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardListScreen(navController: NavController, viewModel: BoardListViewModel) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    val statusBarPadding by animateDpAsState(
        if (topAppBarState.collapsedFraction < .9)
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding().value.dp else 0.dp,
        animationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ContentRoute.ThreadList.route) }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "boards",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "Manage your board selection",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            Icons.Rounded.SelectAll,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                statusBarPadding = statusBarPadding
            )
        },
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            when (viewModel.boardListScreenState.value) {
                is BoardListScreenState.Loading -> LoadingResponse(text = "Loading boards...")
                is BoardListScreenState.Failed ->
                    LoadingResponse(
                        text = "Failed to load boards.\n Please check your internet connection.",
                        failed = true,
                        onClick = { coroutineScope.launch { viewModel.requestBoards() } }
                    )

                is BoardListScreenState.Responded -> {

                    Column {
                        val boardList = viewModel.boardList
                        var boardSearch by remember { mutableStateOf("") }

                        val state = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()

                        SearchBoard(
                            query = boardSearch,
                            onQueryChange = { query ->
                                boardSearch = query
                                viewModel.searchBoards(query)
                                coroutineScope.launch { state.scrollToItem(0) }
                            },
                            topAppBarState = topAppBarState
                        )


                        LazyColumn(state = state) {
                            items(
                                items = boardList,
                                key = { it.board }
                            ) { board ->
                                BoardCard(board = board)
                            }
                        }
                    }

                }
            }
        }
    }
}