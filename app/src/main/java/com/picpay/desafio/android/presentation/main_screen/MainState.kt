package com.picpay.desafio.android.presentation.main_screen

import com.picpay.desafio.android.domain.model.User

sealed class MainState {
    object Loading : MainState()
    data class Success(val users: List<User>) : MainState()
    data class Error(val message: String?) : MainState()
}