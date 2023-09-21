package movee.data.network

import movee.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()

        val url = baseRequest
            .url
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY_TMDB)
            .build()

        val newRequest = baseRequest.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }
}