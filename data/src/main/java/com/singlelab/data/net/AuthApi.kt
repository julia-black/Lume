package com.singlelab.data.net

import com.singlelab.data.model.auth.ResponsePersonUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("authorization/get-code")
    fun sendCode(@Query("phoneNumber") phone: String): Deferred<Response<ResponsePersonUid>>
}