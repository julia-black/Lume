package com.singlelab.net.api

import com.singlelab.net.model.person.PersonUidResponse
import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.model.person.PersonFilledResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("authorization/get-sms-code")
    fun sendCodeAsync(@Query("phoneNumber") phone: String): Deferred<Response<PersonUidResponse>>

    @POST("authorization/set-code")
    fun authAsync(
        @Query("phoneNumber") phone: String,
        @Query("code") code: String
    ): Deferred<Response<AuthResponse>>

    @POST("authorization/get-access-token")
    fun refreshTokenAsync(
        @Query("refreshToken") refreshToken: String,
        @Query("personUid") uid: String
    ): Deferred<Response<AuthResponse>>

    @GET("person/is-person-filled-up")
    fun getIsPersonFilledAsync() : Deferred<Response<PersonFilledResponse>>
}