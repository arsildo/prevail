package com.arsildo.prevail.presentation.screens.content

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.viewModels.ThreadListScreenState
import com.arsildo.prevail.logic.viewModels.ThreadListViewModel
import com.arsildo.prevail.presentation.components.shared.AppBar
import com.arsildo.prevail.presentation.components.shared.LoadingResponse
import com.arsildo.prevail.presentation.components.threadList.BottomSheet
import com.arsildo.prevail.presentation.components.threadList.ThreadCard
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ThreadListScreen(
    navController: NavController, viewModel: ThreadListViewModel,
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
            AppBar(
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { coroutineScope.launch { bottomSheetState.show() } }
                    ) {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                statusBarPadding = statusBarPadding
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
            when (viewModel.threadListScreenState.value) {
                is ThreadListScreenState.Loading -> LoadingResponse(text = "Loading threads...")

                is ThreadListScreenState.Failed -> {
                    LoadingResponse(
                        text = "Failed to load threads.\n Please check your internet connection.",
                        failed = true,
                        onClick = { coroutineScope.launch { viewModel.requestThreads() } }
                    )
                }

                is ThreadListScreenState.Responded -> {

                    val threadList = viewModel.threadList

                    val state = rememberLazyListState()
                    // todo fix behavior and delay of item in focus
                    val itemInFocus by remember {
                        derivedStateOf {
                            val firstVisibleItemIndex = state.firstVisibleItemIndex
                            val visibleItems = state.layoutInfo.visibleItemsInfo

                            val layoutInfo = state.layoutInfo
                            val viewportHeight =
                                layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                            state.layoutInfo.visibleItemsInfo.run {
                                if (isEmpty()) -1
                                else {
                                    if (viewportHeight / 2 == 0) {
                                        firstVisibleItemIndex + (last().index - firstVisibleItemIndex) / 2
                                    } else {
                                        firstVisibleItemIndex / 2
                                    }
                                }
                            }

                        }
                    }

                    LazyColumn(state = state) {
                        itemsIndexed(threadList) { index, thread ->
                            ThreadCard(
                                thread = thread,
                                inFocus = (index == itemInFocus),
                                onClick = { onThreadClicked(thread.no, thread.semantic_url) }
                            )
                        }
                    }


                }

            }
        }
    }
    BottomSheet(bottomSheetState, navController)
}