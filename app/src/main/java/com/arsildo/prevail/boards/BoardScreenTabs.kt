package com.arsildo.prevail.boards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.utils.BangoCat
import com.arsildo.prevail.utils.SavedBoardCard

@Composable
fun BoardsTabRow(
    selectedTabIndex: Int,
    tabList: List<String>,
    onClick: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        indicator = {
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(currentTabPosition = it[selectedTabIndex])
                    .padding(horizontal = 64.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
                color = MaterialTheme.colorScheme.inversePrimary,
                height = 3.dp
            )
        },
        divider = {},
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        tabList.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClick(index) },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(top = 0.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        if (selectedTabIndex == index) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer
                    )
                    .border(
                        width = 2.dp,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.inversePrimary
                        else MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedBoardTabs(
    selectedTabIndex: Int,
    lazyListState: LazyListState,
    boardList: List<Board>,
    savedBoards: List<Board>?,
    onCheckedChange: (Boolean, Board) -> Unit,
) {
    AnimatedContent(
        targetState = selectedTabIndex,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { height -> height } + fadeIn() with
                        slideOutHorizontally { height -> -height } + fadeOut()
            } else {
                slideInHorizontally { height -> -height } + fadeIn() with
                        slideOutHorizontally { height -> height } + fadeOut()
            }.using(SizeTransform(clip = false))
        }
    ) { selectedTab ->
        when (selectedTab) {
            0 -> {
                LazyColumn(state = lazyListState) {
                    items(
                        items = boardList,
                        key = { board -> board.title }
                    ) { board ->
                        BoardCard(
                            board = board,
                            checked = !savedBoards.isNullOrEmpty() && savedBoards.contains(board),
                            onCheckedChange = { checked, boardModel ->
                                onCheckedChange(checked, boardModel)
                            }
                        )
                    }
                }
            }

            else -> {
                if (!savedBoards.isNullOrEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(items = savedBoards) { board ->
                            SavedBoardCard(
                                savedBoard = board,
                                selected = false,
                                onClick = {}
                            )
                        }
                    }
                } else BangoCat()

            }
        }
    }
}
