package com.singlelab.net.interactor

import com.singlelab.net.model.HeaderConst
import com.singlelab.net.model.auth.AuthData
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

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

        if (Locale.getDefault().toLanguageTag() == HeaderConst.RU) {
            requestBuilder.addHeader(HeaderConst.ACCEPT_LANGUAGE, HeaderConst.RU)
        } else {
            requestBuilder.addHeader(HeaderConst.ACCEPT_LANGUAGE, HeaderConst.EN)
        }

        return chain.proceed(requestBuilder.build())
    }
}