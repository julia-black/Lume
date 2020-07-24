package com.singlelab.net.api

import com.singlelab.net.model.MessageResponse
import com.singlelab.net.model.event.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventsApi {

    @GET("event/get-event-list")
    fun getEventsAsync(): Deferred<Response<List<EventSummaryResponse>>>

    @GET("event/get-event")
    fun getEventAsync(@Query("eventUid") uid: String): Deferred<Response<EventResponse>>

    @POST("event/add-event")
    fun addEventAsync(@Body event: EventRequest): Deferred<Response<EventUidResponse>>

    @POST("event/add-event-participant")
    fun addParticipantsAsync(@Body participantRequest: ParticipantRequest): Deferred<Response<MessageResponse>>

    @POST("event/get-random-event")
    fun getRandomEventAsync(@Body randomEventRequest: RandomEventRequest): Deferred<Response<EventResponse>>
}