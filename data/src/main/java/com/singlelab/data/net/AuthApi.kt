package com.singlelab.data.net

import com.singlelab.data.model.auth.ResponseAuth
import com.singlelab.data.model.auth.ResponsePersonUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("api/authorization/get-code")
    fun sendCodeAsync(@Query("phoneNumber") phone: String): Deferred<Response<ResponsePersonUid>>

    @POST("api/authorization/set-code")
    fun authAsync(
        @Query("phoneNumber") phone: String,
        @Query("code") code: String
    ): Deferred<Response<ResponseAuth>>

    @POST("/api/authorization/get-access-token")
    fun refreshTokenAsync(
        @Query("refreshToken") refreshToken: String,
        @Query("personUid") uid: String
    ): Deferred<Response<ResponseAuth>>

}