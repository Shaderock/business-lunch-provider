package com.shaderock.lunch.blpmini.data.datasource.network.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserNetworkModel(
    @SerializedName("id")
    val id: UUID,
    @SerializedName("detailsId")
    val detailsId: UUID,
    @SerializedName("organizationId")
    val organizationId: UUID?,
    @SerializedName("organizationRequestId")
    val organizationRequestId: UUID?,
    @SerializedName("preferencesId")
    val preferencesId: UUID?
)
