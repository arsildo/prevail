package com.arsildo.prevail.presentation.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.viewmodels.MainScreenState
import com.arsildo.prevail.logic.viewmodels.ThreadsViewModel
import com.arsildo.prevail.presentation.components.main.BottomSheet
import com.arsildo.prevail.presentation.components.main.ThreadCard
import com.arsildo.prevail.presentation.components.shared.LoadingResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavController, viewModel: ThreadsViewModel,
    onThreadClicked: (Int, String) -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )
    val statusBarPadding by animateDpAsState(
        if (topAppBarState.collapsedFraction < .99)
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding().value.dp else 0.dp,
        animationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { coroutineScope.launch { bottomSheetState.show() } }
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "/wsg/",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "Worksafe GIF",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { coroutineScope.launch { viewModel.requestThreads() } }
                    ) {
                        Icon(
                            Icons.Rounded.Refresh,
                            contentDescription = null,
                        )
                    }
                    IconButton(
                        onClick = { coroutineScope.launch { bottomSheetState.show() } }
                    ) {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                    .padding(top = statusBarPadding)
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
            when (viewModel.mainScreenState.value) {
                is MainScreenState.Loading -> LoadingResponse(text = "Loading threads...")

                is MainScreenState.Failed -> {
                    LoadingResponse(
                        text = "Failed to load threads.\n Please check your internet connection.",
                        failed = true,
                        onClick = { coroutineScope.launch { viewModel.requestThreads() } }
                    )
                }

                is MainScreenState.Responded -> {

                    val threadList = viewModel.threadList
                    val context = LocalContext.current
                    val listState = rememberLazyListState()
                    val exoPlayer = remember { viewModel.exoPlayer }
                    LazyColumn(state = listState) {
                        items(threadList.size) { it ->
                            threadList[it].threads.forEach { thread ->
                                ThreadCard(
                                    thread = thread,
                                    mediaPlayer = {
                                        Box(modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp))
                                        /*val url =
                                            "https://i.4cdn.org/wsg/${thread.tim}${thread.ext}"
                                        viewModel.playVideo(url)
                                        MediaPlayer(
                                            exoPlayer = exoPlayer,
                                            context = context,
                                        )*/
                                    },
                                    onClick = { onThreadClicked(thread.no, thread.semantic_url) }
                                )
                            }
                        }
                    }


                }

            }
        }
    }
    BottomSheet(bottomSheetState, navController)
}
