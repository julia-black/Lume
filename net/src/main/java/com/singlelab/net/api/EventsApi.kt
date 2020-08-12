package com.singlelab.net.api

import com.singlelab.net.model.MessageResponse
import com.singlelab.net.model.event.*
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.RandomPersonRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface EventsApi {

    @GET("event/get-event-list")
    fun getEventsAsync(): Deferred<Response<List<EventSummaryResponse>>>

    @GET("event/get-event")
    fun getEventAsync(@Query("eventUid") uid: String): Deferred<Response<EventResponse>>

    @POST("event/add-event")
    fun addEventAsync(@Body event: EventRequest): Deferred<Response<EventUidResponse>>

    @POST("event/add-event-participant")
    fun addParticipantsAsync(@Body participantRequest: ParticipantRequest): Deferred<Response<EventResponse>>

    @POST("event/update-event-participant")
    fun updateParticipantsAsync(@Body participantRequest: ParticipantRequest): Deferred<Response<EventResponse>>

    @POST("event/get-random-event")
    fun getRandomEventAsync(@Body randomEventRequest: RandomEventRequest): Deferred<Response<EventResponse>>

    @DELETE("event/remove-event-participant")
    fun removeParticipantsAsync(
        @Query("personUid") personUid: String,
        @Query("eventUid") eventUid: String
    ): Deferred<Response<EventResponse>>

    @POST("event/search-for-event")
    fun searchEventAsync(@Body searchEventRequest: SearchEventRequest): Deferred<Response<List<EventSummaryResponse>>>

    @POST("person/get-random-person")
    fun getRandomPersonAsync(@Body randomPersonRequest: RandomPersonRequest): Deferred<Response<PersonResponse>>

    @POST("event/accept-random-event")
    fun acceptRandomEventAsync(@Body participantRequest: ParticipantRequest): Deferred<Response<MessageResponse>>

    @POST("event/reject-random-event")
    fun rejectRandomEventAsync(@Query("eventUid") eventUid: String): Deferred<Response<MessageResponse>>

    @POST("person/accept-random-person")
    fun acceptRandomPersonAsync(@Body participantRequest: ParticipantRequest): Deferred<Response<MessageResponse>>

    @POST("person/reject-random-person")
    fun rejectRandomPersonAsync(
        @Query("eventUid") eventUid: String,
        @Query("personUid") personUid: String
    ): Deferred<Response<MessageResponse>>
}