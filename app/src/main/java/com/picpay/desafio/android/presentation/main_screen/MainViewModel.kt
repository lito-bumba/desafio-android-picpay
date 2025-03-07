package com.picpay.desafio.android.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state
    private var job: Job? = null

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        job?.cancel()
        job = viewModelScope.launch {
            _state.update { MainState.Loading }
            try {
                repository.getUsers()
                    .asFlow()
                    .flowOn(Dispatchers.IO)
                    .collect { item ->
                        val users = when (val currentState = state.value) {
                            is MainState.Success -> currentState.users
                            else -> emptyList()
                        }
                        _state.update {
                            MainState.Success(users + item)
                        }
                    }
            } catch (exception: Exception) {
                _state.update {
                    MainState.Error(exception.message)
                }
            }
        }
    }
}