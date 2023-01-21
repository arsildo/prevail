package com.arsildo.prevail.threads

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arsildo.prevail.PrevailNavigationActions
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import com.arsildo.prevail.utils.firstFullyVisibleItem
import com.arsildo.prevail.utils.isScrollingUp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ThreadsScreen(
    navController: NavHostController,
    viewModel: ThreadsViewModel,
    onThreadClicked: (Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    val favoriteBoards by viewModel.savedBoards.observeAsState()
    val currentBoard by remember { viewModel.currentBoard }
    val currentBoardDesc by remember { viewModel.currentBoardDesc }

    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndexVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }

    // AppBar
    val appBarState = rememberTopAppBarState()
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

    // Bottom Sheet
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    fun showBottomSheet() = coroutineScope.launch { bottomSheetState.show() }
    fun hideBottomSheet() = coroutineScope.launch { bottomSheetState.hide() }

    // Pull Refresh
    var isRefreshing by remember { mutableStateOf(false) }
    fun pullRefresh() = coroutineScope.launch {
        isRefreshing = true
        delay(1000)
        viewModel.requestThreads()
        isRefreshing = false
        lazyListState.scrollToItem(0)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = ::pullRefresh
    )
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            PrevailAppBar(
                title = {
                    Column {
                        Text(
                            text = "/${currentBoard}/",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = currentBoardDesc,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = ::showBottomSheet) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = null)
                    }
                },
                scrollBehavior = appBarScrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp() && firstVisibleItemIndexVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                fun scrollToTop() = coroutineScope.launch { lazyListState.animateScrollToItem(0) }
                FloatingActionButton(
                    onClick = ::scrollToTop,
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
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .pullRefresh(pullRefreshState),
        ) {
            val screenState by remember { viewModel.screenState }
            Crossfade(targetState = screenState) { state ->
                when (state) {
                    ThreadsScreenState.EmptyBoards -> SelectBoardFirst(onClick = ::showBottomSheet)
                    ThreadsScreenState.Loading -> LoadingAnimation()
                    ThreadsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestThreads)
                    ThreadsScreenState.Responded -> {

                        val threadList = viewModel.threadList

                        val focused = lazyListState.firstFullyVisibleItem()

                        LaunchedEffect(lazyListState.firstFullyVisibleItem()) {
                            viewModel.playerRepository.player.pause()
                            if (threadList[focused].fileExtension == ".webm") {
                                viewModel.playerRepository.playMediaFile(
                                    currentBoard,
                                    threadList[focused].mediaId
                                )
                            }
                        }

                        LazyColumn(
                            state = lazyListState,
                            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(
                                items = threadList,
                                key = { index, thread -> thread.no },
                            ) { index, thread ->
                                ThreadCard(
                                    thread = thread,
                                    playerRepository = viewModel.playerRepository,
                                    inFocus = focused == index,
                                    currentBoard = currentBoard,
                                    onClick = onThreadClicked,
                                    onMediaScreenClick = {
                                        PrevailNavigationActions(navController)
                                            .navigateToMedia(index)
                                    }
                                )
                            }
                        }

                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.TopCenter)
            )

        }

    }



    fun changeBoard(board: String, boardDesc: String) = coroutineScope.launch {
        viewModel.setLastBoard(board, boardDesc)
        delay(500)
        lazyListState.scrollToItem(0)
        hideBottomSheet()
    }

    BottomSheet(
        bottomSheetState = bottomSheetState,
        savedBoards = favoriteBoards,
        currentBoard = currentBoard,
        setLastBoard = ::changeBoard,
        navController = navController
    )

    val context = LocalContext.current
    var timeWhenPressed by remember { mutableStateOf(0L) }
    BackHandler {
        if (bottomSheetState.isVisible) hideBottomSheet()
        else onBackPressedTwice(
            timeWhenPressed = timeWhenPressed,
            context = context
        ) { timeWhenPressed = System.currentTimeMillis() }
    }

}


private fun onBackPressedTwice(
    timeWhenPressed: Long,
    context: Context,
    updateTimeWhenPressed: () -> Unit
) {
    val activityContext = context as Activity
    if (timeWhenPressed + 2000 > System.currentTimeMillis()) activityContext.finish()
    else Toast.makeText(context, "Swipe back once more to leave the app.", Toast.LENGTH_LONG).show()
    updateTimeWhenPressed()
}

@Composable
private fun SelectBoardFirst(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(Modifier.fillMaxWidth()) {
        TextButton(
            onClick = onClick,
            modifier = modifier
                .align(CenterHorizontally)
                .padding(top = 32.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        ) { Text(text = "Please select a board from Quick Actions") }
    }
}