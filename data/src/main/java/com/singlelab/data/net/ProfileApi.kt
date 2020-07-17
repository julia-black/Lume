package com.singlelab.data.net

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Profile
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {

    @GET("core/get-person")
    fun getProfileAsync(): Deferred<Response<Profile>>

    @POST("core/update-person")
    fun updateProfileAsync(@Body profile: Profile): Deferred<Response<ResponseMessage>>
}