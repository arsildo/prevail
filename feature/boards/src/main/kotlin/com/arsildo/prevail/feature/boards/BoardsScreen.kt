package com.arsildo.prevail.feature.boards

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.prevail.feature.boards.search.SearchTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BoardsScreen(
    viewModel: BoardsViewModel = koinViewModel(),
    onBackPress: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var favoriteBoardsVisible by remember { mutableStateOf(false) }
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
                },
                actions = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.StarBorder,
                                contentDescription = null
                            )
                        },
                        onClick = { favoriteBoardsVisible = !favoriteBoardsVisible },
                        colors = if (!favoriteBoardsVisible) IconButtonDefaults.iconButtonColors() else IconButtonDefaults.filledTonalIconButtonColors()
                    )
                },
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
                        Text("favorite board : ${uiState.favoriteBoard}")
                        AnimatedContent(
                            targetState = favoriteBoardsVisible,
                            label = "",
                        ) { target ->
                            when (target) {
                                false -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        val listState = rememberLazyListState()
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
                                            contentPadding = WindowInsets.safeContent.asPaddingValues(),
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            content = {
                                                items(
                                                    items = uiState.boards,
                                                    key = { item -> item.board }
                                                ) { board ->
                                                    BoardCard(
                                                        board = board,
                                                        checked = true,
                                                        onCheckedChange = {
                                                            viewModel.setFavoriteBoard(board.board)
                                                        }
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }

                                else -> {
                                    Column {
                                        Text(
                                            text = "favourite boards",
                                            style = MaterialTheme.typography.displaySmall,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                        LazyVerticalGrid(
                                            columns = GridCells.Fixed(count = 2),
                                            contentPadding = WindowInsets.safeGestures.asPaddingValues(),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
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