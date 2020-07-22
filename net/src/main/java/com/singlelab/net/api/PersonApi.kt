package com.singlelab.net.api

import com.singlelab.net.model.person.ContentRequest
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.model.ImageUidResponse
import com.singlelab.net.model.MessageResponse
import com.singlelab.net.model.person.ProfileResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface PersonApi {

    @GET("person/get-person")
    fun getProfileAsync(): Deferred<Response<ProfileResponse>>

    @GET("person/get-person")
    fun getProfileAsync(@Query("personUid") personUid: String): Deferred<Response<ProfileResponse>>


    @POST("person/update-person")
    fun updateProfileAsync(@Body profile: ProfileRequest): Deferred<Response<MessageResponse>>

    @POST("image/add-person-image")
    fun updateImageProfileAsync(@Body content: ContentRequest): Deferred<Response<ImageUidResponse>>

    @POST("friends/add")
    fun addToFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>

    @DELETE("friends/remove")
    fun removeFromFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>
}