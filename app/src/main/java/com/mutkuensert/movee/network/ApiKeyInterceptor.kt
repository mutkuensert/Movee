package com.mutkuensert.movee.network

import com.mutkuensert.movee.util.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()

        val url = baseRequest
            .url
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val newRequest = baseRequest.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }
}