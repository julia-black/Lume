package com.singlelab.data.net.api

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Content
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface PersonApi {

    @GET("person/get-person")
    fun getProfileAsync(): Deferred<Response<Profile>>

    @GET("person/get-person")
    fun getProfileAsync(@Query("personUid") personUid: String): Deferred<Response<Profile>>


    @POST("person/update-person")
    fun updateProfileAsync(@Body profile: Profile): Deferred<Response<ResponseMessage>>

    @POST("image/add-person-image")
    fun updateImageProfileAsync(@Body content: Content): Deferred<Response<ResponseImageUid>>

    @POST("friends/add")
    fun addToFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<ResponseMessage>>

    @DELETE("friends/remove")
    fun removeFromFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<ResponseMessage>>
}