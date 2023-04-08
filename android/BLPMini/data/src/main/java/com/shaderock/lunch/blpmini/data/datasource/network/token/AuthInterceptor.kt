package com.shaderock.lunch.blpmini.data.datasource.network.token

import com.shaderock.lunch.blpmini.data.datasource.provider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val originalRequest = chain.request()
        var updatedRequest = originalRequest
        val token = tokenProvider.token

        if (tokenProvider.token != null) {
            updatedRequest =
                originalRequest.newBuilder().addHeader("Authorization", "Bearer $token").build()
        }
        return chain.proceed(updatedRequest)
    }
}

