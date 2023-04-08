package com.shaderock.lunch.blpmini.data.datasource.network

sealed class NetworkEndpoint {
    companion object {
        const val BASE: String = "http://10.0.2.2:8080/api/"
        const val USER: String = "user"
        const val LOGIN: String = "login"
        const val PROFILE: String = "$USER/profile"
        const val PROFILE_DETAILS: String = "$PROFILE/details"
    }
}