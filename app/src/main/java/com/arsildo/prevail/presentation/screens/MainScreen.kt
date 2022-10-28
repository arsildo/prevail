package com.arsildo.prevail.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsildo.prevail.logic.navigation.Destinations
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.logic.viewmodels.MainScreenState
import com.arsildo.prevail.presentation.components.ScreenLayout
import com.arsildo.prevail.presentation.components.ThreadCard
import com.arsildo.prevail.presentation.components.ThreadRulesCard

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: BoardsViewModel,
) {

    ScreenLayout {
        when (viewModel.mainScreenState.value) {
            is MainScreenState.Loading -> {
                CircularProgressIndicator()
                Text(text = "Loading", color = MaterialTheme.colorScheme.primary)
            }

            is MainScreenState.Failed -> {
                Text(text = "Failed", color = MaterialTheme.colorScheme.primary)
            }

            is MainScreenState.Responded -> {

                val listState = rememberLazyListState()
                Card(modifier = Modifier.padding(top = 32.dp)) {
                    IconButton(onClick = { navController.navigate(Destinations.Preferences.route) }) {
                        Icon(Icons.Rounded.Settings, contentDescription = null)
                    }
                }
                LazyColumn(state = listState) {
                    items(viewModel.threadList.value.size) { index ->
                        if (index == 0) ThreadRulesCard(thread = viewModel.threadList.value[0].threads[0])
                        else
                            viewModel.threadList.value[index].threads.forEach {
                                ThreadCard(thread = it)
                            }
                    }
                }
            }
        }
    }
}
