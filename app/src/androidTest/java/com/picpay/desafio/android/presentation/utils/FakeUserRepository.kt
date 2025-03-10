package com.picpay.desafio.android.presentation.utils

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {

    private var isError: Boolean = false

    fun setError() {
        this.isError = true
    }

    override suspend fun getUsers(): Result<Flow<List<User>>> {
        if (isError) {
            return Result.failure(Exception("test-error"))
        }

        val users = listOf(
            User(
                id = 1,
                name = "test-name",
                username = "test-username",
                img = "test-img"
            )
        )
        val usersFlow = flow { emit(users) }
        return Result.success(usersFlow)
    }

    override suspend fun syncUsers(): Result<Unit> {
        if (isError) {
            return Result.failure(Exception("test-error"))
        }

        return Result.success(Unit)
    }
}