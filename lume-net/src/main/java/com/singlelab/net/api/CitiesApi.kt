package com.singlelab.net.api

import com.singlelab.net.model.city.CityResponse
import com.singlelab.net.model.promo.PromoRewardResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesApi {

    @GET("city/get-cities")
    fun getCitiesAsync(): Deferred<Response<List<CityResponse>>>

    @GET("city/check-city-for-promo-reward")
    fun checkCityForPromoRewardAsync(@Query("cityId") cityId: Int): Deferred<Response<PromoRewardResponse>>
}