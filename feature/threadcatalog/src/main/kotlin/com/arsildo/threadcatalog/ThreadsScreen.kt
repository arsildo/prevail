package com.arsildo.threadcatalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThreadsScreen(
    viewModel: ThreadCatalogViewModel = koinViewModel(),
    onThreadClick: (Int) -> Unit,
    onBoardsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "/gif/") },
                actions = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        onClick = onBoardsClick
                    )
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = null
                            )
                        },
                        onClick = onPreferencesClick
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        },
        contentWindowInsets = WindowInsets(top = 0, bottom = 0),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) CircularProgressIndicator(strokeCap = StrokeCap.Round)
            else {
                if (uiState.loadingError.isBlank())
                    Column {
                        val firstVisibleItem by remember { derivedStateOf { listState.firstVisibleItemIndex } }
                        val midIndex by remember(firstVisibleItem) {
                            derivedStateOf {
                                listState.layoutInfo.visibleItemsInfo.run {
                                    val firstVisibleIndex = listState.firstVisibleItemIndex
                                    if (isEmpty()) -1 else firstVisibleIndex + (last().index - firstVisibleIndex) / 2
                                }
                            }
                        }
                        LazyColumn(
                            state = listState,
                            contentPadding = WindowInsets.safeGestures.asPaddingValues(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            itemsIndexed(
                                items = uiState.threads[0].threads,
                                key = { index, thread -> thread.no }
                            ) { index, thread ->
                                ThreadCard(
                                    thread = thread,
                                    mediaContent = {
                                        Box(
                                            Modifier
                                                .clip(CardDefaults.elevatedShape)
                                                .aspectRatio(1f)
                                                .background(if (index == midIndex) Color.Green else Color.Blue)
                                        )
                                    },
                                    onClick = { onThreadClick(thread.no) }
                                )
                            }
                        }
                    }
                else Text(text = uiState.loadingError, textAlign = TextAlign.Center)
            }
        }
    }
}
