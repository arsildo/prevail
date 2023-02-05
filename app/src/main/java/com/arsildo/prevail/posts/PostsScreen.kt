package com.arsildo.prevail.posts

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.threads.LocalBoardContext
import com.arsildo.prevail.utils.LoadingAnimation
import com.arsildo.prevail.utils.MediaPlayer
import com.arsildo.prevail.utils.PrevailAppBar
import com.arsildo.prevail.utils.RetryConnectionButton
import com.arsildo.prevail.utils.firstFullyVisibleItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PostsScreen(
    navController: NavController,
    viewModel: PostsViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    val threadNumber = viewModel.threadNumber
    val currentBoard by remember { viewModel.currentBoard }

    val playerRepository = remember { viewModel.playerRepository }
    val mediaPlayer = remember { playerRepository.player }

    val appBarState = rememberTopAppBarState()
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = appBarState)

    val lazyListState = rememberLazyListState()

    var refreshing by remember { mutableStateOf(false) }
    fun refreshPostList() = coroutineScope.launch {
        refreshing = true
        delay(1000)
        viewModel.requestThread()
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = ::refreshPostList
    )

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
                    Column {
                        Text(
                            text = "/$currentBoard/",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "$threadNumber",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Rounded.BookmarkAdd, contentDescription = null)
                    }
                },
                scrollBehavior = appBarScrollBehavior,
            )
        },
        floatingActionButton = {
            val uriHandler = LocalUriHandler.current
            FloatingActionButton(
                onClick = { viewModel.openThreadInBrowser(uriHandler) },
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(2.dp),
                modifier = Modifier.padding(16.dp)
            ) { Icon(imageVector = Icons.Rounded.Forum, contentDescription = null) }
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
            CompositionLocalProvider(LocalBoardContext provides currentBoard) {
                val screenState by remember { viewModel.screenState }
                Crossfade(targetState = screenState) { state ->
                    when (state) {
                        PostsScreenState.Loading -> LoadingAnimation()
                        PostsScreenState.Failed -> RetryConnectionButton(onClick = viewModel::requestThread)
                        PostsScreenState.Responded -> {

                            val postList = viewModel.postList

                            val focused = lazyListState.firstFullyVisibleItem()

                            LaunchedEffect(focused) {
                                mediaPlayer.pause()
                                if (postList[focused].mediaType == ".webm") {
                                    mediaPlayer.seekTo(focused, 0)
                                }
                            }

                            LazyColumn(
                                state = lazyListState,
                                contentPadding = PaddingValues(vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(
                                    items = postList,
                                    key = { _, post -> post.no },
                                ) { index, post ->
                                    PostCard(
                                        post = post,
                                        playableMedia = {
                                            MediaPlayer(
                                                focused = index == focused,
                                                playerRepository = playerRepository
                                            )
                                        }
                                    )
                                }
                            }

                        }
                    }
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