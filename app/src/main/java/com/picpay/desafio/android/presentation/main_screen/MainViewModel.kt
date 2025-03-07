package com.picpay.desafio.android.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.di.AppModule
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
            delay(TIME_TO_FETCH_DATA)
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

    companion object {
        private const val TIME_TO_FETCH_DATA = 300L

        fun create() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val service = AppModule.service
                val repository = UserRepositoryImpl(service)
                return MainViewModel(repository) as T
            }
        }
    }
}