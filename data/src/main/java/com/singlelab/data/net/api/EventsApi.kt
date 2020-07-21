package com.singlelab.data.net.api

import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.EventSummary
import com.singlelab.data.model.event.ResponseEventUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventsApi {

    @GET("event/get-event-list")
    fun getEventsAsync(): Deferred<Response<List<EventSummary>>>

    @GET("event/get-event")
    fun getEventAsync(@Query("eventUid") uid: String): Deferred<Response<Event>>

    @POST("event/add-event")
    fun addEventAsync(@Body event: Event): Deferred<Response<ResponseEventUid>>
}