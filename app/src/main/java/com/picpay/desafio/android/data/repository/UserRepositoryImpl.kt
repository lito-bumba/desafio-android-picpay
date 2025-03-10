package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.local.UserDao
import com.picpay.desafio.android.data.local.mapper.toUserEntities
import com.picpay.desafio.android.data.local.mapper.toUsers
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.remote.mapper.toUsers
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val dao: UserDao,
    private val service: PicPayService
) : UserRepository {

    override suspend fun getUsers(): Flow<List<User>> {
        val userEntities = service.getUsers().toUsers()
        dao.saveAll(userEntities.toUserEntities())
        return dao.getAll().map { it.toUsers() }
    }
}