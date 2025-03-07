package com.picpay.desafio.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("img") val img: String,
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String
)