package com.shaderock.lunch.blpmini.data.repository

import com.shaderock.lunch.blpmini.data.datasource.disk.auth.TokenDiskModel
import com.shaderock.lunch.blpmini.data.datasource.disk.auth.TokenDiskSource
import com.shaderock.lunch.blpmini.data.datasource.memory.auth.TokenMemoryModel
import com.shaderock.lunch.blpmini.data.datasource.memory.auth.TokenMemorySource
import com.shaderock.lunch.blpmini.data.datasource.network.token.TokenNetworkSource
import com.shaderock.lunch.blpmini.data.datasource.provider.TokenProvider
import com.shaderock.lunch.blpmini.domain.gateway.AuthGateway
import com.shaderock.lunch.blpmini.domain.model.LoginDataModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val tokenMemorySource: TokenMemorySource,
    private val tokenDiskSource: TokenDiskSource,
    private val tokenNetworkSource: TokenNetworkSource,
    private val tokenProvider: TokenProvider
) : AuthGateway {
    override fun login(loginData: LoginDataModel) = flow {
        val tokenNetworkModel = tokenNetworkSource.getToken(
            TokenNetworkSource.LoginForm(
                loginData.email,
                loginData.password
            )
        )
        tokenMemorySource.data = TokenMemoryModel(tokenNetworkModel.token)
        tokenDiskSource.data = TokenDiskModel(tokenNetworkModel.token)
        emit(Unit)
    }

    override fun hasToken(): Boolean {
        return tokenProvider.token != null
    }

    override fun logout() = flow {
        tokenDiskSource.data = null
        tokenMemorySource.data = null
        emit(Unit)
    }
}