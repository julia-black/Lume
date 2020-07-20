package com.singlelab.data.net.api

import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.RequestPersonFilled
import com.singlelab.data.model.auth.RequestPersonUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("authorization/get-code")
    fun sendCodeAsync(@Query("phoneNumber") phone: String): Deferred<Response<RequestPersonUid>>

    @POST("authorization/set-code")
    fun authAsync(
        @Query("phoneNumber") phone: String,
        @Query("code") code: String
    ): Deferred<Response<Auth>>

    @POST("authorization/get-access-token")
    fun refreshTokenAsync(
        @Query("refreshToken") refreshToken: String,
        @Query("personUid") uid: String
    ): Deferred<Response<Auth>>

    @GET("/api/core/is-person-filled-up")
    fun getIsPersonFilledAsync() : Deferred<Response<RequestPersonFilled>>

}