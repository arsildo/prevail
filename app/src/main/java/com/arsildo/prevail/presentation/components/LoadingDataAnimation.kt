package com.arsildo.prevail.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingDataAnimation() {
    Column {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.then(Modifier.size(60.dp)),
                strokeWidth = 4.dp
            )
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.then(Modifier.size(80.dp)),
                strokeWidth = 4.dp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Loading...", color = MaterialTheme.colorScheme.primary)
    }
}