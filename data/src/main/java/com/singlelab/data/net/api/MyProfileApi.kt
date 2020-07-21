package com.singlelab.data.net.api

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Content
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyProfileApi {

    @GET("person/get-person")
    fun getProfileAsync(): Deferred<Response<Profile>>

    @POST("person/update-person")
    fun updateProfileAsync(@Body profile: Profile): Deferred<Response<ResponseMessage>>

    @POST("image/add-person-image")
    fun updateImageProfileAsync(@Body content: Content): Deferred<Response<ResponseImageUid>>
}