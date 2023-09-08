package com.arsildo.prevail.feature.boards.search

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
internal fun SearchTextField(
    modifier: Modifier = Modifier,
    onClearQuery: () -> Unit,
    onQueryValueChange: (String) -> Unit,
    query: String = "",
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryValueChange,
        placeholder = { Text(text = "Search", style = MaterialTheme.typography.labelLarge) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) IconButton(
                onClick = onClearQuery,
                content = {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null
                    )
                }
            )
        },
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = 16.dp,
                start = WindowInsets.safeContent
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = WindowInsets.safeContent
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection = LayoutDirection.Ltr)
            ),
    )
}