package com.picpay.desafio.android.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    private var syncJob: Job? = null

    init {
        fetchUsers()
        syncUsers(true)
    }

    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            delay(300)
            repository.getUsers().onSuccess { usersFlow ->
                usersFlow.collect { users ->
                    _state.update {
                        it.copy(
                            users = users,
                            isLoading = false
                        )
                    }
                }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        error = exception.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun syncUsers(isFirstTime: Boolean = false) {
        if (state.value.error != null) {
            fetchUsers()
            return
        }

        syncJob?.cancel()
        syncJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = isFirstTime,
                    isSyncing = !isFirstTime
                )
            }

            if (!isFirstTime) {
                delay(300)
            }
            repository.syncUsers()
                .onSuccess {
                    if (!isFirstTime) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isSyncing = false
                            )
                        }
                    }
                }.onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            syncError = exception.message,
                            isSyncing = false
                        )
                    }
                }
        }
    }
}