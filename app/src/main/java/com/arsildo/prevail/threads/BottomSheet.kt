package com.arsildo.prevail.threads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.HorizontalRule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.ContentScreens
import com.arsildo.prevail.PrevailDestinations
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.utils.SavedBoardCard


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    bottomSheetState: ModalBottomSheetState,
    savedBoards: List<Board>?,
    currentBoard: String,
    setLastBoard: (String, String) -> Unit,
    navController: NavController
) {
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
        sheetContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        sheetShape = MaterialTheme.shapes.large,
        sheetElevation = 8.dp,
        sheetContent = {
            Icon(
                Icons.Rounded.HorizontalRule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                if (!savedBoards.isNullOrEmpty()) {
                    CategoryLabel(text = "Saved Boards")
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(savedBoards) { board ->
                            SavedBoardCard(
                                savedBoard = board,
                                selected = board.board == currentBoard,
                                onClick = {
                                    if (board.board != currentBoard)
                                        setLastBoard(board.board, board.title)
                                }
                            )
                        }
                    }
                }

                CategoryLabel(text = "More Actions")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    userScrollEnabled = false,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        OptionCard(icon = Icons.Rounded.Dashboard, title = "Your Boards") {
                            navController.navigate(ContentScreens.BOARDS_SCREEN)
                        }
                    }
                    item {
                        OptionCard(icon = Icons.Outlined.Bookmarks, title = "Saved Threads") {
                            navController.navigate(PrevailDestinations.PREFERENCES_ROUTE)
                        }
                    }
                    item {
                        OptionCard(icon = Icons.Outlined.Settings, title = "Preferences") {
                            navController.navigate(PrevailDestinations.PREFERENCES_ROUTE)
                        }
                    }
                }

            }
        },
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionCard(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
fun CategoryLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(start = 8.dp)
    )
}