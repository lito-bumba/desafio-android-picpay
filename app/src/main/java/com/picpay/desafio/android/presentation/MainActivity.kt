package com.picpay.desafio.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.picpay.desafio.android.presentation.main_screen.MainScreenRoot
import com.picpay.desafio.android.presentation.main_screen.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModel.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenRoot(viewModel)
        }
    }
}
