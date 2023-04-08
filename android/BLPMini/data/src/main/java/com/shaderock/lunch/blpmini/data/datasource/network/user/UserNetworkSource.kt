package com.shaderock.lunch.blpmini.data.datasource.network.user

import com.shaderock.lunch.blpmini.data.datasource.network.NetworkEndpoint
import retrofit2.http.GET

interface UserNetworkSource {
    @GET(NetworkEndpoint.PROFILE)
    suspend fun getUser(): UserNetworkModel
}