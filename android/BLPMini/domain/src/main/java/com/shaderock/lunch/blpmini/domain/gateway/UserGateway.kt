package com.shaderock.lunch.blpmini.domain.gateway

import com.shaderock.lunch.blpmini.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserGateway {
    fun getUser(): Flow<UserEntity>
}