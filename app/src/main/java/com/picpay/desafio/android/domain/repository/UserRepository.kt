package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUsers(): Result<Flow<List<User>>>

    suspend fun syncUsers(): Result<Unit>
}