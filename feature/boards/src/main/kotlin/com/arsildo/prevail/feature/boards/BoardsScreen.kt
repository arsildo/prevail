package com.arsildo.prevail.feature.boards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.prevail.feature.boards.search.SearchTextField
import com.arsildo.utils.isScrollingUp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BoardsScreen(
    viewModel: BoardsViewModel = koinViewModel(),
    onBackPress: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val firstVisibleItem by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Boards") },
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        },
                        onClick = onBackPress
                    )
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = firstVisibleItem > 0 && listState.isScrollingUp(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowUpward,
                            contentDescription = null
                        )
                    },
                    onClick = { coroutineScope.launch { listState.animateScrollToItem(0) } },
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(end = 16.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(top = 0, bottom = 0),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) CircularProgressIndicator(strokeCap = StrokeCap.Round)
            else {
                if (uiState.loadingError.isBlank())
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var selectedIndex by remember { mutableIntStateOf(0) }
                        val tabs = listOf("all boards", "favorites")
                        BoardTabs(
                            tabs = tabs,
                            selectedIndex = selectedIndex,
                            onTabSelected = { tab -> selectedIndex = tab }
                        )
                        AnimatedContent(
                            targetState = selectedIndex,
                            label = "",
                        ) { target ->
                            when (target) {
                                0 -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        LaunchedEffect(uiState.boards.size) {
                                            listState.scrollToItem(0)
                                        }
                                        SearchTextField(
                                            query = uiState.searchQuery,
                                            onQueryValueChange = viewModel::updateSearchQuery,
                                            onClearQuery = viewModel::clearSearchQuery
                                        )
                                        LazyColumn(
                                            state = listState,
                                            contentPadding = WindowInsets.safeGestures.asPaddingValues(),
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            content = {
                                                items(
                                                    items = uiState.boards,
                                                    key = { item -> item.board }
                                                ) { board ->
                                                    BoardCard(
                                                        board = board,
                                                        onCheckedChange = {}
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }

                                else -> {
                                    Column(modifier = Modifier.fillMaxSize()) {
                                        LazyColumn(
                                            contentPadding = WindowInsets.safeGestures.asPaddingValues(),
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            content = {
                                                items(count = 6, key = { it }) {
                                                    FavoriteBoardCard()
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}