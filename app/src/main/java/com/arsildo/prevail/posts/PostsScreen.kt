package com.arsildo.prevail.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.MediaPlayerDialog
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PostsScreen(
    navController: NavController,
    viewModel: PostsViewModel,
) {

    val threadNumber = viewModel.threadNumber
    val currentBoard by viewModel.currentBoard

    val coroutineScope = rememberCoroutineScope()

    val appBarState = rememberTopAppBarState()
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = appBarState)

    val lazyListState = rememberLazyListState()

    var refreshing by remember { mutableStateOf(false) }
    fun refreshPostList() = coroutineScope.launch {
        refreshing = true
        delay(1000)
        viewModel.requestThread()
        refreshing = false
        lazyListState.animateScrollToItem(viewModel.postList.lastIndex)
    }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refreshPostList)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            PrevailAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ContentScreens.THREADS_SCREEN) }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Text(
                        text = "$currentBoard - $threadNumber",
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Rounded.BookmarkAdd,
                            contentDescription = null,
                        )
                    }
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
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .pullRefresh(pullRefreshState)
        ) {
            when (viewModel.postsScreenState.value) {
                is PostsScreenState.Loading -> LoadingAnimation()
                is PostsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestThread)

                is PostsScreenState.Responded -> {
                    val postList = viewModel.postList


                    val midIndex by remember {
                        derivedStateOf {
                            lazyListState.layoutInfo.visibleItemsInfo.run {
                                val firstVisibleIndex = lazyListState.firstVisibleItemIndex
                                if (isEmpty()) -1 else firstVisibleIndex + (last().index - firstVisibleIndex) / 2
                            }
                        }
                    }

                    LaunchedEffect(midIndex) {
                        viewModel.playerRepository.player.pause()
                        if (postList[midIndex].ext == ".webm")
                            viewModel.playerRepository.playMediaFile(
                                currentBoard,
                                postList[midIndex].tim
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
                            items = postList,
                            key = { index, post -> post.no },
                        ) { index, post ->
                            PostCard(
                                post = post,
                                playerRepository = viewModel.playerRepository,
                                inFocus = midIndex == index,
                                currentBoard = currentBoard,
                                onPlayVideoNotInFocus = { mediaID, aspectRatio ->
                                    viewModel.playerRepository.playMediaFile(currentBoard, mediaID)
                                    aspectRatioMediaPlayer = aspectRatio
                                    mediaPlayerDialogVisible = true
                                }

                            )
                        }
                    }

                    MediaPlayerDialog(
                        visible = mediaPlayerDialogVisible,
                        videoAspectRatio = aspectRatioMediaPlayer,
                        playerRepository = viewModel.playerRepository,
                        currentBoard = currentBoard,
                        onDismissRequest = {
                            mediaPlayerDialogVisible = false
                            viewModel.playerRepository.player.clearMediaItems()
                            viewModel.playerRepository.player.clearVideoSurface()
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
}