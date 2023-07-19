package com.arsildo.threadcatalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ThreadsScreen(
    viewModel: ThreadsViewModel = koinViewModel(),
    testValue: Boolean,
    onThreadClick: (Int) -> Unit,
    onPreferencesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ThreadsScreenPreview(
        uiState = uiState,
        onThreadClick = onThreadClick,
        testValue = testValue,
        onPreferencesClick = onPreferencesClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThreadsScreenPreview(
    uiState: ThreadsScreenUiState,
    testValue: Boolean,
    onThreadClick: (Int) -> Unit,
    onPreferencesClick: () -> Unit,
) {
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
                        Text(text = testValue.toString())
                        Button(
                            onClick = { onThreadClick(0) }
                        ) {
                            Text(text = "Update")
                        }
                        /*LazyColumn(
                            state = listState,
                            contentPadding = WindowInsets.safeGestures.asPaddingValues(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                items = uiState.threads[0].threads,
                                key = { item -> item.no }
                            ) { thread ->
                                ThreadCard(
                                    thread = thread,
                                    onClick = { onThreadClick(thread.no) }
                                )
                            }
                        }*/
                    }
                else Text(text = uiState.loadingError, textAlign = TextAlign.Center)
            }
        }
    }
}
