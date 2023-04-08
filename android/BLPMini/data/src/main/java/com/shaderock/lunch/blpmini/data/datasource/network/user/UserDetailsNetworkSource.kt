package com.shaderock.lunch.blpmini.data.datasource.network.user

import com.shaderock.lunch.blpmini.data.datasource.network.NetworkEndpoint
import retrofit2.http.GET

interface UserDetailsNetworkSource {

    @GET(NetworkEndpoint.PROFILE_DETAILS)
    suspend fun getUserDetails(): UserDetailsNetworkModel
}