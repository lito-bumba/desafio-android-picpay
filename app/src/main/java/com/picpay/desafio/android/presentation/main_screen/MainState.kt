package com.picpay.desafio.android.presentation.main_screen

import com.picpay.desafio.android.domain.model.User

data class MainState(
    val isLoading : Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
    val isSyncing: Boolean = false,
    val syncError: String? = null
)