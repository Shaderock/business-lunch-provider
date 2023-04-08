package com.shaderock.lunch.blpmini.domain.gateway

import com.shaderock.lunch.blpmini.domain.entity.UserDetailsEntity
import kotlinx.coroutines.flow.Flow

interface UserDetailsGateway {
    fun getUserDetails(): Flow<UserDetailsEntity>
}
