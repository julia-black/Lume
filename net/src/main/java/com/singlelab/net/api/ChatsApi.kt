package com.singlelab.net.api

import com.singlelab.net.model.chat.ChatMessageRequest
import com.singlelab.net.model.chat.ChatMessageResponse
import com.singlelab.net.model.chat.ChatPreviewResponse
import com.singlelab.net.model.chat.ChatResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatsApi {
    @GET("chat/get-chat")
    fun chat(
        @Query("chatUid") chatUid: String
    ): Deferred<Response<ChatResponse>>

    @GET("chat/get-person-chat")
    fun chatWithPerson(
        @Query("personUid") personUid: String
    ): Deferred<Response<ChatResponse>>

    @GET("chat/get-person-chat-list")
    fun chats(): Deferred<Response<List<ChatPreviewResponse>>>

    @POST("chat/add-chat-message")
    fun sendMessage(@Body message: ChatMessageRequest): Deferred<Response<ChatMessageResponse>>
}