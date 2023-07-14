package com.arsildo.threadcatalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arsildo.model.Thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThreadCard(
    thread: Thread,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = thread.name.toString())
                Text(text = thread.no.toString())
            }
            Box(
                modifier = Modifier.aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = thread.ext.toString())
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = thread.replies.toString())
                Text(text = thread.no.toString())
            }
        }
    }
}