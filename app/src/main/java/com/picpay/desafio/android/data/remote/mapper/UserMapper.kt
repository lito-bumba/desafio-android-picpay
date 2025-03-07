package com.picpay.desafio.android.data.remote.mapper

import com.picpay.desafio.android.data.remote.dto.UserDto
import com.picpay.desafio.android.domain.model.User

fun UserDto.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        username = this.username,
        img = this.img
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        username = this.username,
        img = this.img
    )
}