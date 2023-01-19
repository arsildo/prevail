package com.arsildo.prevail.boards

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import com.arsildo.prevail.utils.SearchBoard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsScreen(navController: NavController, viewModel: BoardsViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val appBarState = rememberTopAppBarState()
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

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
                            text = "Manage your boards",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) { Icon(Icons.Rounded.SelectAll, contentDescription = null) }
                    IconButton(
                        onClick = viewModel::removeAllBoards,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) { Icon(Icons.Rounded.DeleteSweep, contentDescription = null) }
                },
                scrollBehavior = appBarScrollBehavior,
            )
        },
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            val screenState by remember { viewModel.screenState }
            Crossfade(
                targetState = screenState,
                animationSpec = tween(durationMillis = 1000)
            ) { state ->
                when (state) {
                    BoardsScreenState.Loading -> LoadingAnimation()
                    BoardsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestBoards)
                    BoardsScreenState.Responded -> {
                        Column {

                            val savedBoards by viewModel.savedBoards.observeAsState()
                            val boardList = viewModel.boardList
                            val boardSearch by viewModel.searchBoard

                            fun searchBoard(query: String) = coroutineScope.launch {
                                viewModel.searchBoards(query)
                                lazyListState.scrollToItem(0)
                            }
                            SearchBoard(
                                query = boardSearch,
                                onQueryChange = ::searchBoard,
                                topAppBarState = appBarState
                            )

                            val tabList = listOf("Available Boards", "Saved Boards")
                            var selectedTabIndex by remember { mutableStateOf(0) }

                            BoardsTabRow(
                                selectedTabIndex = selectedTabIndex,
                                tabList = tabList,
                                onClick = { selectedTabIndex = it }
                            )

                            AnimatedBoardTabs(
                                selectedTabIndex = selectedTabIndex,
                                lazyListState = lazyListState,
                                boardList = boardList,
                                savedBoards = savedBoards,
                                onCheckedChange = viewModel::removeIfChecked
                            )

                        }
                    }
                }
            }
        }
    }
}