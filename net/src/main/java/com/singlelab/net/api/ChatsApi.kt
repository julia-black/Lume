package com.singlelab.net.api

import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import com.singlelab.net.model.chat.ChatResponse
import com.singlelab.net.model.chat.ChatMessagesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatsApi {
    @GET("chat/get-chat")
    fun chatAsync(
        @Query("chatUid") chatUid: String
    ): Deferred<Response<ChatMessagesResponse>>

    @GET("chat/get-person-chat")
    fun chatWithPersonAsync(
        @Query("personUid") personUid: String
    ): Deferred<Response<ChatMessagesResponse>>

    @GET("chat/get-person-chat-list")
    fun chatsAsync(): Deferred<Response<List<ChatResponse>>>

    @POST("chat/add-chat-message")
    fun sendMessageAsync(@Body message: ChatMessageRequest): Deferred<Response<ChatMessageResponse>>
}