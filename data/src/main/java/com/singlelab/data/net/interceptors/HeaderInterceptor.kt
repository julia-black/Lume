package com.singlelab.data.net.interceptors

import com.singlelab.data.model.auth.Headers
import com.singlelab.data.model.consts.HeaderConst
import okhttp3.Interceptor
import okhttp3.Response


class HeaderInterceptor(private val headers: Headers?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()

        val requestBuilder = originalRequest.newBuilder()
            .url(originalHttpUrl)

        if (!headers?.accessToken.isNullOrEmpty()) {
            requestBuilder.addHeader(HeaderConst.ACCESS_TOKEN, headers!!.accessToken!!)
        }

        if (!headers?.uid.isNullOrEmpty()) {
            requestBuilder.addHeader(HeaderConst.UID, headers!!.uid!!)
        }

        return chain.proceed(requestBuilder.build())
    }
}