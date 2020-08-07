package com.singlelab.net.api

import com.singlelab.net.model.chat.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatsApi {
    @GET("chat/get-new-chat-messages")
    fun loadNewMessageAsync(
        @Query("chatUid") chatUid: String,
        @Query("messageUid") lastMessageUid: String?
    ): Deferred<Response<List<ChatMessageResponse>>>

    @GET("chat/get-chat")
    fun chatAsync(
        @Query("chatUid") chatUid: String,
        @Query("pageNumber") pageNumber: Int? = 1,
        @Query("pageSize") pageSize: Int? = 1
    ): Deferred<Response<ChatMessagesResponse>>

    @GET("chat/get-person-chat")
    fun chatWithPersonAsync(
        @Query("personUid") personUid: String,
        @Query("pageNumber") pageNumber: Int? = 1,
        @Query("pageSize") pageSize: Int? = 10
    ): Deferred<Response<ChatMessagesResponse>>

    @GET("chat/get-person-chat-list")
    fun chatsAsync(): Deferred<Response<List<ChatResponse>>>

    @POST("chat/add-chat-message")
    fun sendMessageAsync(@Body message: ChatMessageRequest): Deferred<Response<ChatMessageResponse>>
}