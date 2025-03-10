package com.picpay.desafio.android.presentation.utils

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

class FakeUserRepository : UserRepository {

    private var isError: Boolean = false

    fun setError() {
        this.isError = true
    }

    override suspend fun getUsers(): List<User> {
        if (isError) {
            throw Exception("test-error")
        }

        return listOf(
            User(
                id = 1,
                name = "test-name",
                username = "test-username",
                img = "test-img"
            )
        )
    }
}