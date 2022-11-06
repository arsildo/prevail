package com.arsildo.prevail.presentation.components.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CollapsingTopAppBar(
    appBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
            .animateContentSize(
                tween(
                    delayMillis = 0,
                    durationMillis = 256,
                    easing = LinearOutSlowInEasing,
                )
            )
            .padding(
                if (appBarState.collapsedFraction < 1) PaddingValues(
                    top = (32 + 4).dp
                ) else PaddingValues(top = 0.dp)
            ),
        navigationIcon = navigationIcon,
        title = title,
        actions = actions,
        windowInsets = if (WindowInsets.isImeVisible) WindowInsets.waterfall else WindowInsets.ime,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
    )
}