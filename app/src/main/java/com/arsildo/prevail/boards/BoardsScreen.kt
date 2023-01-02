package com.arsildo.prevail.boards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
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
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.presentation.components.boardList.BoardCard
import com.arsildo.prevail.presentation.components.boardList.SearchBoard
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(navController: NavController, viewModel: BoardsViewModel) {

    val coroutineScope = rememberCoroutineScope()

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            PrevailAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ContentScreens.THREADS_SCREEN) }) {
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
            when (viewModel.boardsScreenState.value) {
                is BoardsScreenState.Loading -> LoadingAnimation()
                is BoardsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestBoards)

                is BoardsScreenState.Responded -> {

                    Column {
                        val boardList = viewModel.boardList
                        var boardSearch by remember { mutableStateOf("") }

                        val state = rememberLazyListState()

                        SearchBoard(
                            query = boardSearch,
                            onQueryChange = { query ->
                                boardSearch = query
                                viewModel.searchBoards(query)
                                coroutineScope.launch { state.animateScrollToItem(0) }
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