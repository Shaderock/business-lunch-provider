package com.shaderock.lunch.blpmini.di

import com.shaderock.lunch.blpmini.data.datasource.disk.auth.TokenDiskSource
import com.shaderock.lunch.blpmini.data.datasource.memory.auth.TokenMemorySource
import com.shaderock.lunch.blpmini.data.datasource.memory.user.UserDetailsMemorySource
import com.shaderock.lunch.blpmini.data.datasource.memory.user.UserMemorySource
import com.shaderock.lunch.blpmini.data.datasource.network.token.TokenNetworkSource
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserDetailsNetworkSource
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserNetworkSource
import com.shaderock.lunch.blpmini.data.datasource.provider.TokenProvider
import com.shaderock.lunch.blpmini.data.repository.AuthRepository
import com.shaderock.lunch.blpmini.data.repository.UserDetailsRepository
import com.shaderock.lunch.blpmini.data.repository.UserRepository
import com.shaderock.lunch.blpmini.domain.gateway.AuthGateway
import com.shaderock.lunch.blpmini.domain.gateway.UserDetailsGateway
import com.shaderock.lunch.blpmini.domain.gateway.UserGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun providesUserGateWay(
        memorySource: UserMemorySource,
        networkSource: UserNetworkSource
    ): UserGateway {
        return UserRepository(memorySource, networkSource)
    }

    @Provides
    fun providesUserDetailsGateWay(
        memorySource: UserDetailsMemorySource,
        networkSource: UserDetailsNetworkSource
    ): UserDetailsGateway {
        return UserDetailsRepository(memorySource, networkSource)
    }

    @Provides
    fun providesAuthGateWay(
        tokenMemorySource: TokenMemorySource,
        diskSource: TokenDiskSource,
        networkSource: TokenNetworkSource,
        tokenProvider: TokenProvider
    ): AuthGateway {
        return AuthRepository(tokenMemorySource, diskSource, networkSource, tokenProvider)
    }
}