package com.singlelab.net.interactor

import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.HeaderConst
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()

        val requestBuilder = originalRequest.newBuilder()
            .url(originalHttpUrl)

        if (!AuthData.accessToken.isNullOrEmpty()) {
            requestBuilder.addHeader(HeaderConst.ACCESS_TOKEN, AuthData.accessToken!!)
        }

        if (!AuthData.uid.isNullOrEmpty()) {
            requestBuilder.addHeader(HeaderConst.UID, AuthData.uid!!)
        }

        return chain.proceed(requestBuilder.build())
    }
}