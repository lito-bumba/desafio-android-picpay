package com.picpay.desafio.android.data.local.mapper

import com.picpay.desafio.android.data.local.UserEntity
import com.picpay.desafio.android.domain.model.User

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        name = this.name,
        username = this.username,
        img = this.img
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        username = this.username,
        img = this.img
    )
}