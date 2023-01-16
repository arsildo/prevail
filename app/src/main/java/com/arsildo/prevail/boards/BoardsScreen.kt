package com.arsildo.prevail.boards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.R
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import com.arsildo.prevail.utils.SavedBoardCard
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
            Crossfade(targetState = screenState) { screen ->
                when (screen) {
                    BoardsScreenState.Loading -> LoadingAnimation()
                    BoardsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestBoards)
                    BoardsScreenState.Responded -> {

                        Column {

                            val boardList = viewModel.boardList
                            val boardSearch by viewModel.searchBoard

                            val savedBoards by viewModel.savedBoards.observeAsState()


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

@Composable
fun BoardsTabRow(
    selectedTabIndex: Int,
    tabList: List<String>,
    onClick: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        indicator = {
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(currentTabPosition = it[selectedTabIndex])
                    .padding(horizontal = 64.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
                color = MaterialTheme.colorScheme.inversePrimary,
                height = 3.dp
            )
        },
        divider = {},
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        tabList.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClick(index) },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(top = 0.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        if (selectedTabIndex == index) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer
                    )
                    .border(
                        width = 2.dp,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.inversePrimary
                        else MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedBoardTabs(
    selectedTabIndex: Int,
    lazyListState: LazyListState,
    boardList: List<Board>,
    savedBoards: List<Board>?,
    onCheckedChange: (Boolean, Board) -> Unit,
) {
    AnimatedContent(
        targetState = selectedTabIndex,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { height -> height } + fadeIn() with
                        slideOutHorizontally { height -> -height } + fadeOut()
            } else {
                slideInHorizontally { height -> -height } + fadeIn() with
                        slideOutHorizontally { height -> height } + fadeOut()
            }.using(SizeTransform(clip = false))
        }
    ) { selectedTab ->
        when (selectedTab) {
            0 -> {
                LazyColumn(state = lazyListState) {
                    items(
                        items = boardList,
                        key = { board -> board.title }
                    ) { board ->
                        BoardCard(
                            board = board,
                            checked = !savedBoards.isNullOrEmpty() && savedBoards.contains(board),
                            onCheckedChange = { checked, boardModel ->
                                onCheckedChange(checked, boardModel)
                            }
                        )
                    }
                }
            }

            else -> {
                if (!savedBoards.isNullOrEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(items = savedBoards) { board ->
                            SavedBoardCard(
                                savedBoard = board,
                                selected = false,
                                onClick = {}
                            )
                        }
                    }
                } else BangoCat()

            }
        }
    }
}

@Composable
fun BangoCat() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.bongocat),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxHeight(.5f),
        )
        Text(
            text = "there is nothing here :(",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge
        )
    }
}