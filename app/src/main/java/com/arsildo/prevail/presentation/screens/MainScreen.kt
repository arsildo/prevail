package com.arsildo.prevail.presentation.screens

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.network.models.threads.Thread
import com.arsildo.prevail.logic.network.models.threads.ThreadCatalogItem
import com.arsildo.prevail.logic.viewmodels.MainScreenState
import com.arsildo.prevail.logic.viewmodels.ThreadsViewModel
import com.arsildo.prevail.presentation.components.main.BottomSheet
import com.arsildo.prevail.presentation.components.main.ThreadCard
import com.arsildo.prevail.presentation.components.shared.CollapsingTopAppBar
import com.arsildo.prevail.presentation.components.shared.ScreenLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.launch
import java.lang.Math.abs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavController, viewModel: ThreadsViewModel) {


    when (viewModel.mainScreenState.value) {
        is MainScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            )
            { Text(text = "Loading", color = Color.White) }
        }

        is MainScreenState.Failed -> {
            Text(text = "Failed", color = MaterialTheme.colorScheme.primary)
        }

        is MainScreenState.Responded -> {
            val threadList = viewModel.threadList.value
            val appBarState = rememberTopAppBarState()
            val scrollBehavior =
                TopAppBarDefaults.enterAlwaysScrollBehavior(
                    state = appBarState,
                    snapAnimationSpec = tween(
                        easing = LinearOutSlowInEasing,
                        durationMillis = 1024,
                        delayMillis = 0
                    )

                )

            val bottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = true
            )
            val coroutineScope = rememberCoroutineScope()

            ScreenLayout {
                CollapsingTopAppBar(
                    appBarState = appBarState,
                    scrollBehavior = scrollBehavior,
                    title = {
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "/board/",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = "board desc",
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { bottomSheetState.show() }
                            }
                        ) {
                            Icon(
                                Icons.Rounded.MoreVert,
                                contentDescription = null
                            )
                        }
                    }
                )
                MainBody(threadList = threadList, scrollBehavior = scrollBehavior)
            }

            BottomSheet(bottomSheetState, navController)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBody(threadList: List<ThreadCatalogItem>, scrollBehavior: TopAppBarScrollBehavior) {
    val context = LocalContext.current
    // Keep a single instance of ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }
    val listState = rememberLazyListState()
    val playingItem = determineCurrentlyPlayingItem(listState, threadList[0].threads)
    updateCurrentlyPlayingItem(exoPlayer = exoPlayer, thread = playingItem)
    LazyColumn(
        state = listState,
        modifier = Modifier
            .animateContentSize(
                tween(
                    delayMillis = 0,
                    durationMillis = 128,
                    easing = LinearOutSlowInEasing
                )
            )
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        // TODO Either add pagination or combine all Threads in single list
        // At the moment we are just doing this for the first list of threads hence threadList[0]
        items(threadList[0].threads.size) { index ->
                ThreadCard(thread = threadList[0].threads[index],
                    if(playingItem != null && threadList[0].threads[index].no == playingItem.no) exoPlayer else null)
        }
    }

    //TODO handle lifecycle events
}

fun determineCurrentlyPlayingItem(listState: LazyListState, items: List<Thread>) : Thread? {
    val layoutInfo = listState.layoutInfo
    val visibleThreads = layoutInfo.visibleItemsInfo.map { items[it.index] }
    val threadsWithVideo = visibleThreads.filter { it.hasAnimatedMedia }
    return if (threadsWithVideo.size == 1) {
        threadsWithVideo.first()
    } else {
        val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
        val itemsFromCenter =
            layoutInfo.visibleItemsInfo.sortedBy { abs((it.offset + it.size / 2) - midPoint) }
        itemsFromCenter.map { items[it.index] }.firstOrNull { it.hasAnimatedMedia }
    }
}

@Composable
private fun updateCurrentlyPlayingItem(exoPlayer: ExoPlayer, thread: Thread?) {
    LaunchedEffect(thread) {
        exoPlayer.apply {
            if (thread != null) {
                setMediaItem(MediaItem.fromUri(Uri.parse("https://i.4cdn.org/wsg/${thread.tim}${thread.ext}")))
                prepare()
                playWhenReady = true
            } else {
                stop()
            }
        }
    }
}