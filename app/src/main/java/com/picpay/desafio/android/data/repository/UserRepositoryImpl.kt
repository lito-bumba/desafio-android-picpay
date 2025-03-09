package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.remote.mapper.toUser
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

class UserRepositoryImpl(
    private val service: PicPayService
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        val users = service.getUsers()
        return users.map { it.toUser() }
    }
}