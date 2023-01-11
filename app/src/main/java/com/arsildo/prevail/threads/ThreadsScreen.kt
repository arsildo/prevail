package com.arsildo.prevail.threads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.MediaPlayerDialog
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun ThreadsScreen(
    navController: NavController,
    viewModel: ThreadsViewModel,
    onThreadClicked: (Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    val topAppBarState = rememberTopAppBarState()
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val lastBoard by viewModel.boardPreferencesRepository.currentBoard
    val lastBoardDesc by viewModel.boardPreferencesRepository.currentBoardDesc

    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndexVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }

    var refreshing by remember { mutableStateOf(false) }
    fun pullRefresh() = coroutineScope.launch {
        refreshing = true
        delay(1000)
        viewModel.requestThreads()
        refreshing = false
        lazyListState.scrollToItem(0)
        delay(500)
        bottomSheetState.hide()
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = ::pullRefresh
    )



    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            PrevailAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "/${lastBoard}/",
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Text(
                            text = lastBoardDesc,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { coroutineScope.launch { bottomSheetState.show() } }) {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = topAppBarScrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp() && firstVisibleItemIndexVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { coroutineScope.launch { lazyListState.animateScrollToItem(0) } },
                    shape = MaterialTheme.shapes.large,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp),
                    modifier = Modifier.padding(16.dp)
                ) { Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = null) }
            }
        },
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .pullRefresh(pullRefreshState),
        ) {
            if (lastBoard == "empty") Text(text = "Please add board(s)")
            else when (viewModel.screenState.value) {
                is ThreadsScreenState.Loading -> LoadingAnimation()
                is ThreadsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestThreads)
                is ThreadsScreenState.Responded -> {

                    val threadList = viewModel.threadList

                    val centerScreenItem by remember {
                        derivedStateOf {
                            lazyListState.layoutInfo.visibleItemsInfo.run {
                                val firstVisibleIndex = lazyListState.firstVisibleItemIndex
                                if (isEmpty()) -1 else firstVisibleIndex + (last().index - firstVisibleIndex) / 2
                            }
                        }
                    }
                    LaunchedEffect(centerScreenItem) {
                        viewModel.playerRepository.player.pause()
                        if (threadList[centerScreenItem].ext == ".webm")
                            viewModel.playerRepository.playMediaFile(
                                currentBoard = lastBoard,
                                mediaID = threadList[centerScreenItem].tim
                            )
                    }

                    var mediaPlayerDialogVisible by remember { mutableStateOf(false) }
                    var aspectRatioMediaPlayer by remember { mutableStateOf(1f) }

                    LazyColumn(
                        state = lazyListState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(
                            items = threadList,
                            key = { index, thread -> thread.no },
                        ) { index, thread ->
                            ThreadCard(
                                thread = thread,
                                playerRepository = viewModel.playerRepository,
                                inFocus = centerScreenItem == index,
                                currentBoard = lastBoard,
                                onClick = { onThreadClicked(thread.no) },
                                onPlayVideoNotInFocus = { mediaID, aspectRatio ->
                                    viewModel.playerRepository.playMediaFile(
                                        currentBoard = lastBoard,
                                        mediaID = mediaID
                                    )
                                    aspectRatioMediaPlayer = aspectRatio
                                    mediaPlayerDialogVisible = true
                                },
                            )
                        }
                    }

                    MediaPlayerDialog(
                        visible = mediaPlayerDialogVisible,
                        videoAspectRatio = aspectRatioMediaPlayer,
                        playerRepository = viewModel.playerRepository,
                        currentBoard = lastBoard,
                        onDismissRequest = {
                            mediaPlayerDialogVisible = false
                            viewModel.playerRepository.clearPlayer()
                        }
                    )

                }


            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.TopCenter)
            )

        }
    }
    val favoriteBoards by viewModel.savedBoards.observeAsState()
    BottomSheet(
        bottomSheetState = bottomSheetState,
        savedBoards = favoriteBoards,
        lastBoard = lastBoard,
        setLastBoard = { boardName, boardDesc ->
            viewModel.setLastBoard(board = boardName, boardDesc = boardDesc)
            pullRefresh()
        },
        navController = navController
    )

}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}