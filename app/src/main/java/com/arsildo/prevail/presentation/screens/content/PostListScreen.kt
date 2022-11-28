package com.arsildo.prevail.presentation.screens.content

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.navigation.ContentRoute
import com.arsildo.prevail.logic.viewModels.PostListScreenState
import com.arsildo.prevail.logic.viewModels.PostListViewModel
import com.arsildo.prevail.presentation.components.shared.AppBar
import com.arsildo.prevail.presentation.components.shared.LoadingResponse
import com.arsildo.prevail.presentation.components.threadPosts.PostCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel,
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

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(ContentRoute.ThreadList.route)
                        }
                    ) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = viewModel.threadName,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                        Text(
                            text = "${viewModel.threadNumber}",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { viewModel.requestThread(viewModel.threadNumber) }
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Refresh,
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
            when (viewModel.postListScreenState.value) {
                is PostListScreenState.Loading -> LoadingResponse(text = "Loading thread...")

                is PostListScreenState.Failed -> {
                    LoadingResponse(
                        text = "Failed to load thread.\n Please check your internet connection.",
                        failed = true,
                        onClick = { coroutineScope.launch { viewModel.requestThread(viewModel.threadNumber) } }
                    )
                }

                is PostListScreenState.Responded -> {

                    val postList = viewModel.postList.posts
                    LazyColumn {
                        items(postList.size) {
                            postList.forEach { post -> PostCard(post = post) }
                        }
                    }

                }

            }
        }
    }
}