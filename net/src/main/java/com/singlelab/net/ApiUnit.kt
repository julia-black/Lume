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

class ApiUnit(baseUrl: String) {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HeaderInterceptor())
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

    val chatsApi: ChatsApi = retrofit.create(
        ChatsApi::class.java
    )
}