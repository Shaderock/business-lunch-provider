package com.shaderock.lunch.blpmini.domain.usecase

import com.shaderock.lunch.blpmini.domain.entity.Role
import com.shaderock.lunch.blpmini.domain.gateway.AuthGateway
import com.shaderock.lunch.blpmini.domain.gateway.UserDetailsGateway
import com.shaderock.lunch.blpmini.domain.gateway.UserGateway
import com.shaderock.lunch.blpmini.domain.model.LoginDataModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AuthUseCase(
    private val authGateway: AuthGateway,
    private val userGateway: UserGateway,
    private val userDetailsGateway: UserDetailsGateway
) {
    fun login(email: String, password: String) = flow {
        authGateway.login(LoginDataModel(email, password)).collect { }
        userGateway.getUser().collect { }
        userDetailsGateway.getUserDetails().collect { }
        emit(Unit)
    }

    fun isAuthorized() = flow {
        if (!authGateway.hasToken()) {
            emit(false)
            return@flow
        }

        emit(userDetailsGateway.getUserDetails().first().roles.contains(Role.EMPLOYEE))
    }
}