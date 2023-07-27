package com.mutkuensert.movee.network

import com.mutkuensert.movee.domain.login.RequiresAccountIdError
import com.mutkuensert.movee.library.user.UserManager
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AccountIdInterceptor @Inject constructor(private val userManager: UserManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()

        val urlBuilder = baseRequest.url.newBuilder()
        val segments = baseRequest.url.pathSegments

        for (index in segments.indices) {
            if (PathParameters.ACCOUNT_ID == segments[index]) {
                val accountId = userManager.getCurrentUser()?.id?.toString()
                    ?: throw RequiresAccountIdError("Account id could not be found.")

                urlBuilder.setPathSegment(index, accountId)
            }
        }

        val newRequest = baseRequest.newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(newRequest)
    }
}