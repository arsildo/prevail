package com.arsildo.prevail.utils

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBoard(
    query: String,
    onQueryChange: (String) -> Unit,
    topAppBarState: TopAppBarState,
) {
    val focusManager = LocalFocusManager.current

    val topPadding by animateDpAsState(
        if (topAppBarState.collapsedFraction < .99) 0.dp
        else WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
        animationSpec = tween(delayMillis = 0, easing = LinearOutSlowInEasing)
    )

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search board",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        textStyle = MaterialTheme.typography.titleMedium,
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        shape = MaterialTheme.shapes.extraLarge,
        singleLine = true,
        colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            cursorColor = MaterialTheme.colorScheme.onTertiaryContainer,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.tertiary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() },
            onDone = { focusManager.clearFocus() }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(top = topPadding)
    )
}

