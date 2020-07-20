package com.singlelab.data.net.api

import com.singlelab.data.model.event.Event
import com.singlelab.data.model.event.EventSummary
import com.singlelab.data.model.event.ResponseEventUid
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventsApi {

    @GET("event/get-event-list")
    fun getEventsAsync(): Deferred<Response<List<EventSummary>>>

    @POST("event/add-event")
    fun addEventAsync(@Body event: Event): Deferred<Response<ResponseEventUid>>
}