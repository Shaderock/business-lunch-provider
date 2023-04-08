package com.shaderock.lunch.blpmini.domain.gateway

import com.shaderock.lunch.blpmini.domain.model.LoginDataModel
import kotlinx.coroutines.flow.Flow

interface AuthGateway {
    fun login(loginData: LoginDataModel): Flow<Unit>
    fun hasToken(): Boolean
    fun logout(): Flow<Unit>
}