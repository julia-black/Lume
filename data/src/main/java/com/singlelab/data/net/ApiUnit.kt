package com.singlelab.data.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.singlelab.data.model.consts.Const.BASE_URL
import com.singlelab.data.net.interceptors.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUnit {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)

    val profileApi: ProfileApi = retrofit.create(ProfileApi::class.java)
}