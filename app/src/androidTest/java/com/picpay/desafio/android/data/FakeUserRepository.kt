package com.picpay.desafio.android.data

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {

    private var isGetUsersError: Boolean = false
    private var isSyncUsersError: Boolean = false

    fun setGetUsersError() {
        this.isGetUsersError = true
    }

    fun setSyncUsersError() {
        this.isSyncUsersError = true
    }

    override suspend fun getUsers(): Result<Flow<List<User>>> {
        if (isGetUsersError) {
            return Result.failure(Exception("get-users-test-error"))
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
        if (isSyncUsersError) {
            return Result.failure(Exception("sync-users-test-error"))
        }

        return Result.success(Unit)
    }
}