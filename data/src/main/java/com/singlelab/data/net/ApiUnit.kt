package com.singlelab.data.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUnit {

    private const val BASE_URL = "https://lumewebapp.azurewebsites.net/api/"

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            //.addQueryParameter("api_key", AppConstants.tmdbApiKey)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}