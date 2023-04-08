package com.shaderock.lunch.blpmini.data.datasource.network.user

import com.google.gson.annotations.SerializedName
import com.shaderock.lunch.blpmini.domain.entity.Role
import java.util.*

data class UserDetailsNetworkModel(
    @SerializedName("id")
    val id: UUID,
    @SerializedName("appUserId")
    val appUserId: UUID,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("roles")
    val roles: Set<Role>
)