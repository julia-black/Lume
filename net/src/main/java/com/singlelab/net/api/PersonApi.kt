package com.singlelab.net.api

import com.singlelab.net.model.ImageUidResponse
import com.singlelab.net.model.MessageResponse
import com.singlelab.net.model.person.ContentRequest
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.model.person.ProfileResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface PersonApi {

    @GET("person/get-profile")
    fun getProfileAsync(): Deferred<Response<ProfileResponse>>

    @GET("person/get-person")
    fun getProfileAsync(@Query("personUid") personUid: String): Deferred<Response<ProfileResponse>>

    @GET("person/get-person-list")
    fun searchPersonAsync(
        @Query("filter") searchStr: String,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Deferred<Response<List<PersonResponse>>>

    @POST("person/update-person")
    fun updateProfileAsync(@Body profile: ProfileRequest): Deferred<Response<MessageResponse>>

    @POST("image/add-person-image")
    fun updateImageProfileAsync(@Body content: ContentRequest): Deferred<Response<ImageUidResponse>>

    @GET("friends/get-friends")
    fun getFriendsAsync(@Query("personUid") personUid: String): Deferred<Response<List<PersonResponse>>>

    @POST("friends/add")
    fun addToFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>

    @DELETE("friends/remove")
    fun removeFromFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>
}