package com.picpay.desafio.android.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.main_screen.component.UserItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenRoot(
    viewModel: MainViewModel = koinViewModel<MainViewModel>()
) {
    val state = viewModel.state.collectAsState()
    MainScreen(state = state.value)
}

@Composable
private fun MainScreen(state: MainState) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            MainState.Loading -> {
                CircularProgressIndicator()
            }

            is MainState.Success -> {
                LazyColumn {
                    items(state.users) { user ->
                        UserItem(
                            user = user,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                }
            }

            is MainState.Error -> {
                Text(text = state.message ?: stringResource(R.string.error))
            }
        }
    }
}