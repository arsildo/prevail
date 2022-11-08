package com.arsildo.prevail.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.viewmodels.MainScreenState
import com.arsildo.prevail.logic.viewmodels.ThreadsViewModel
import com.arsildo.prevail.presentation.components.main.BottomSheet
import com.arsildo.prevail.presentation.components.main.ThreadCard
import com.arsildo.prevail.presentation.components.shared.CollapsingTopAppBar
import com.arsildo.prevail.presentation.components.shared.LoadingResponse
import com.arsildo.prevail.presentation.components.shared.ScreenLayout
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavController, viewModel: ThreadsViewModel) {


    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topBarState,
        snapAnimationSpec = tween(delayMillis = 0, easing = LinearEasing)
    )

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val coroutineScope = rememberCoroutineScope()

    ScreenLayout(
        topBar = {
            CollapsingTopAppBar(
                appBarState = topBarState,
                scrollBehavior = scrollBehavior,
                title = {
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { coroutineScope.launch { bottomSheetState.show() } }
                            .padding(horizontal = 8.dp)
                    ) {
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
                            coroutineScope.launch { viewModel.requestThreads() }
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Refresh,
                            contentDescription = null
                        )
                    }
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
        }
    ) {
        when (viewModel.mainScreenState.value) {
            is MainScreenState.Loading -> {
                LoadingResponse(text = "Loading threads...")
            }

            is MainScreenState.Failed -> {
                LoadingResponse(
                    text = "Failed to load data.\n Please check your internet connection.",
                    reloadEnabled = true,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.requestThreads()
                        }
                    }
                )
            }

            is MainScreenState.Responded -> {
                val threadList = viewModel.threadList.value
                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                ) {
                    items(viewModel.threadList.value.size) { index ->
                        threadList[index].threads.forEach {
                            ThreadCard(thread = it)
                        }
                    }
                }

            }

        }
    }
    BottomSheet(bottomSheetState, navController)
}
