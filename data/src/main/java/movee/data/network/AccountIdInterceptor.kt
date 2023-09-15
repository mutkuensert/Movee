package movee.data.network

import javax.inject.Inject
import movee.domain.library.UserManager
import movee.domain.login.RequiresAccountIdError
import okhttp3.Interceptor
import okhttp3.Response

class AccountIdInterceptor @Inject constructor(private val userManager: UserManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()

        val urlBuilder = baseRequest.url.newBuilder()
        val segments = baseRequest.url.pathSegments

        for (index in segments.indices) {
            if (PathParameters.ACCOUNT_ID == segments[index]) {
                val accountId = userManager.getUserId()?.toString()
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