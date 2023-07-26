package com.mutkuensert.movee.network

import com.mutkuensert.movee.util.BEARER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()

        /*val url = baseRequest
            .url
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val newRequest = baseRequest.newBuilder().url(url).build()*/

        val newRequest = baseRequest.newBuilder()
            .addHeader("Authorization", "Bearer $BEARER_TOKEN")
            .build()

        return chain.proceed(newRequest)
    }
}