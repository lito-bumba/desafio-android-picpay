package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.local.UserDao
import com.picpay.desafio.android.data.local.mapper.toUser
import com.picpay.desafio.android.data.local.mapper.toUserEntity
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.remote.mapper.toUser
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val dao: UserDao,
    private val service: PicPayService
) : UserRepository {

    override suspend fun getUsers(): Result<Flow<List<User>>> {
        return try {
            val usersFlow = dao.getAll().map { usersEntity ->
                usersEntity.map { it.toUser() }
            }
            Result.success(usersFlow)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun syncUsers(): Result<Unit> {
        return try {
            val users = service.getUsers().map { it.toUser() }
            val userEntities = users.map { it.toUserEntity() }
            dao.saveAll(userEntities)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}