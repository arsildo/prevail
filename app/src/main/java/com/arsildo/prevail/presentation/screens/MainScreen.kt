package com.arsildo.prevail.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.arsildo.prevail.logic.viewmodels.BoardsViewModel
import com.arsildo.prevail.logic.viewmodels.MainScreenState
import com.arsildo.prevail.presentation.components.LoadingDataAnimation
import com.arsildo.prevail.presentation.components.ScreenLayout
import com.arsildo.prevail.presentation.components.main_screen.RulesCard
import com.arsildo.prevail.presentation.components.main_screen.ThreadCard

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: BoardsViewModel,
) {


    when (viewModel.mainScreenState.value) {
        is MainScreenState.Loading -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            )
            {
                LoadingDataAnimation()
            }
        }

        is MainScreenState.Failed -> {
            Text(text = "Failed", color = MaterialTheme.colorScheme.primary)
        }

        is MainScreenState.Responded -> {
            val listState = rememberLazyListState()

            ScreenLayout {
                LazyColumn(state = listState) {
                    items(viewModel.threadList.value.size) { index ->
                        if (index == 0) RulesCard(thread = viewModel.threadList.value[0].threads[0])
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
