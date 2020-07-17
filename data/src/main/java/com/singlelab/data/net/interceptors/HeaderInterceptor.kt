package com.singlelab.data.net.interceptors

import com.singlelab.data.model.auth.AuthData
import com.singlelab.data.model.consts.HeaderConst
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