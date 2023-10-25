package com.arsildo.prevail.feature.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
internal fun BoardTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = MaterialTheme.shapes.large,
) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = WindowInsets.safeGestures
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
            .background(containerColor, shape)
            .fillMaxWidth()
    ) {
        PrimaryTabRow(
            tabs = {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { onTabSelected(index) },
                        content = {
                            Text(
                                text = tab,
                                color = contentColor,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        },
                        modifier = Modifier.clip(shape)
                    )
                }
            },
            selectedTabIndex = selectedIndex,
            divider = {},
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(currentTabPosition = it[selectedIndex])
                        .background(contentColor.copy(.09F), shape),
                    height = 300.dp,
                    color = Color.Transparent
                )
            },
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        )
    }
}