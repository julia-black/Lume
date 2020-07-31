package com.singlelab.net.api

import com.singlelab.net.model.city.CityResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CitiesApi {

    @GET("city/get-cities")
    fun getCitiesAsync(): Deferred<Response<List<CityResponse>>>
}