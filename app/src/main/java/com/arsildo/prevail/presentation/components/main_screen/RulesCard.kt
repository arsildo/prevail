package com.arsildo.prevail.presentation.components.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arsildo.prevail.logic.network.model.thread_catalog.Thread

@Composable
fun RulesCard(thread: Thread) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${thread.no}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = thread.now,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = thread.semantic_url,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Checkout the rules and catalog first.",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}