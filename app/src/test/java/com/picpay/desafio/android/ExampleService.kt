package com.picpay.desafio.android

import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.remote.dto.UserDto

class ExampleService(
    private val service: PicPayService
) {

    fun example(): List<UserDto> {
        val users = service.getUsers().execute()

        return users.body() ?: emptyList()
    }
}