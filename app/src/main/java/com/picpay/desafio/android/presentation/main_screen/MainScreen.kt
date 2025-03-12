@file:OptIn(ExperimentalMaterial3Api::class)

package com.picpay.desafio.android.presentation.main_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.main_screen.component.UserItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel<MainViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {},
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        PullToRefreshBox(
            isRefreshing = state.value.isSyncing,
            onRefresh = {
                viewModel.syncUsers { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    when {
                        state.value.isLoading == true -> {
                            CircularProgressIndicator(
                                color = colorResource(R.color.colorAccent),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .testTag(stringResource(R.string.progress_indicator))
                            )
                        }

                        state.value.error != null -> {
                            Text(
                                text = state.value.error ?: stringResource(R.string.error),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                items(state.value.users) { user ->
                                    UserItem(
                                        user = user,
                                        modifier = Modifier
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}