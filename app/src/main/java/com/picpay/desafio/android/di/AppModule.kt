package com.picpay.desafio.android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.presentation.main_screen.MainViewModel
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"
private val retrofit: PicPayService by lazy {
    val gson: Gson by lazy { GsonBuilder().create() }
    val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    retrofit.create(PicPayService::class.java)
}


val appModule = module {
    singleOf(::retrofit).bind<PicPayService>()

    factoryOf(::UserRepositoryImpl).bind<UserRepository>()

    viewModelOf(::MainViewModel)
}