package com.arsildo.prevail.boards

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arsildo.prevail.data.Board
import com.arsildo.prevail.utils.ContentCardWrapper
import com.arsildo.prevail.utils.HtmlText

@Composable
fun BoardCard(
    board: Board,
    checked: Boolean,
    onCheckedChange: (Boolean, Board) -> Unit,
) {
    ContentCardWrapper(onClick = { onCheckedChange(checked, board) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth(.8f)) {
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(.2f)),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                ) {
                    Text(
                        text = "/${board.board}/",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = board.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                HtmlText(htmlText = board.meta_description)
            }

            Checkbox(checked = checked, onCheckedChange = { onCheckedChange(checked, board) })

        }
    }
}