package com.shaderock.lunch.blpmini.data.datasource.network.token

import com.shaderock.lunch.blpmini.data.datasource.network.NetworkEndpoint
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenNetworkSource {
    @POST(NetworkEndpoint.LOGIN)
    suspend fun getToken(@Body loginForm: LoginForm): TokenNetworkModel

    data class LoginForm(val email: String, val password: String)
}
