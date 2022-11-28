package com.arsildo.prevail.presentation.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior,
    statusBarPadding: Dp,
) {
    TopAppBar(
        windowInsets = WindowInsets(0, 0, 0, 0),
        scrollBehavior = scrollBehavior,
        navigationIcon = navigationIcon,
        title = title,
        actions = actions,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .padding(top = statusBarPadding)
    )
}