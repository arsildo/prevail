package com.arsildo.prevail.boards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.utils.SearchBoard
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
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
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
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Rounded.SelectAll,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = viewModel::removeAllBoards) {
                        Icon(
                            Icons.Rounded.DeleteSweep,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
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
            when (viewModel.screenState.value) {
                is BoardsScreenState.Loading -> LoadingAnimation()
                is BoardsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestBoards)

                is BoardsScreenState.Responded -> {

                    Column {
                        val boardList = viewModel.boardList
                        val boardSearch by viewModel.searchBoard
                        val savedBoards by viewModel.savedBoards.observeAsState()

                        val lazyListState = rememberLazyListState()

                        SearchBoard(
                            query = boardSearch,
                            onQueryChange = { query ->
                                viewModel.searchBoards(query)
                                coroutineScope.launch { lazyListState.scrollToItem(0) }
                            },
                            topAppBarState = topAppBarState
                        )


                        LazyColumn(state = lazyListState) {
                            itemsIndexed(
                                items = boardList,
                                key = { index, board -> board.title }
                            ) { index, board ->
                                BoardCard(
                                    board = board,
                                    checked = !savedBoards.isNullOrEmpty() && savedBoards!!.contains(board),
                                    onCheckedChange = { checked ->
                                        if (checked) viewModel.removeFromSavedBoards(board)
                                        else viewModel.insertToSavedBoards(board)
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}