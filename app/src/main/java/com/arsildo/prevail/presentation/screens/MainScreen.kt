package com.arsildo.prevail.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.viewmodels.MainScreenState
import com.arsildo.prevail.logic.viewmodels.ThreadsViewModel
import com.arsildo.prevail.presentation.components.main.BottomSheet
import com.arsildo.prevail.presentation.components.main.ThreadCard
import com.arsildo.prevail.presentation.components.shared.CollapsingTopAppBar
import com.arsildo.prevail.presentation.components.shared.ScreenLayout
import kotlinx.coroutines.launch

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
            val listState = rememberLazyListState()
            val appBarState = rememberTopAppBarState()
            val scrollBehavior =
                TopAppBarDefaults.enterAlwaysScrollBehavior(
                    state = appBarState,
                    snapAnimationSpec = tween(
                        delayMillis = 0,
                        durationMillis = 128,
                        easing = LinearOutSlowInEasing,
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
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .animateContentSize(
                            tween(
                                delayMillis = 0,
                                durationMillis = 256,
                                easing = LinearOutSlowInEasing,
                            )
                        )
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                ) {
                    items(viewModel.threadList.value.size) { index ->
                        threadList[index].threads.forEach {
                            ThreadCard(thread = it)
                        }
                    }
                }
            }

            BottomSheet(bottomSheetState, navController)

        }
    }
}

