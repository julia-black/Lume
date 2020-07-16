package com.singlelab.data.net

import com.singlelab.data.model.Empty
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface MyProfileApi {

    @GET("home/secure")
    fun getHomeAsync(): Deferred<Response<Empty>> //todo временный запрос для теста
}