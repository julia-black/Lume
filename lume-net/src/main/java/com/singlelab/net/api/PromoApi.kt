package com.singlelab.net.api

import com.singlelab.net.model.promo.PromoInfoResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface PromoApi {
    @GET("promo/get-promo")
    fun getPromoAsync(): Deferred<Response<PromoInfoResponse>>
}