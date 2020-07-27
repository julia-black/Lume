package com.singlelab.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.singlelab.net.api.AuthApi
import com.singlelab.net.api.ChatsApi
import com.singlelab.net.api.EventsApi
import com.singlelab.net.api.PersonApi
import com.singlelab.net.interactor.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiUnit(baseUrl: String) {

    private val headerInterceptor = HeaderInterceptor()

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val authApi: AuthApi = retrofit.create(
        AuthApi::class.java
    )

    val personApi: PersonApi = retrofit.create(
        PersonApi::class.java
    )

    val eventsApi: EventsApi = retrofit.create(
        EventsApi::class.java
    )

    val chatsApi: ChatsApi = Retrofit.Builder()
        .client(
            OkHttpClient()
                .newBuilder()
                .readTimeout(105, TimeUnit.SECONDS)
                .connectTimeout(125, TimeUnit.SECONDS)
                .callTimeout(145, TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        )
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ChatsApi::class.java)
}