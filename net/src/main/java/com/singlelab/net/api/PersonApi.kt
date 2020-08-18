package com.singlelab.net.api

import com.singlelab.net.model.MessageResponse
import com.singlelab.net.model.person.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface PersonApi {

    @GET("person/get-profile")
    fun getProfileAsync(): Deferred<Response<ProfileResponse>>

    @GET("person/get-person")
    fun getProfileAsync(@Query("personUid") personUid: String): Deferred<Response<ProfileResponse>>

    @POST("person/get-person-list")
    fun searchPersonAsync(@Body request: SearchPersonRequest): Deferred<Response<List<PersonResponse>>>

    @POST("person/update-person")
    fun updateProfileAsync(@Body profile: ProfileRequest): Deferred<Response<ProfileResponse>>

    @POST("person/remove-person-token")
    fun removePushTokenAsync(): Deferred<Response<ProfileResponse>>

    @GET("person/get-person-notifications")
    fun getNotificationsAsync(): Deferred<Response<PersonNotificationsResponse>>

    @POST("person/add-feedback")
    fun addFeedbackAsync(@Body request: FeedbackRequest): Deferred<Response<MessageResponse>>

    @GET("friends/get-friends")
    fun getFriendsAsync(@Query("personUid") personUid: String): Deferred<Response<List<PersonResponse>>>

    @POST("friends/add-friend")
    fun addToFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>

    @DELETE("friends/remove-friend")
    fun removeFromFriendsAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>

    @POST("friends/confirm-friend")
    fun confirmFriendAsync(@Query("friendGuid") personUid: String): Deferred<Response<MessageResponse>>
}