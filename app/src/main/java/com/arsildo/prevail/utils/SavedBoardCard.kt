package com.arsildo.prevail.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arsildo.prevail.data.Board

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedBoardCard(
    savedBoard: Board,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        label = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
                    .animateContentSize(tween(256))
            ) {
                Text(
                    text = "/${savedBoard.board}/",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = savedBoard.title,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        leadingIcon = {
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(),
                exit = fadeOut(tween(durationMillis = 0))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.background,
            selectedBorderColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            borderWidth = 0.dp,
            selectedBorderWidth = 1.5.dp
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    )

}