package com.shaderock.lunch.blpmini.data.datasource.network.token

import com.google.gson.annotations.SerializedName

data class TokenNetworkModel(
    @SerializedName("token")
    val token: String
)
