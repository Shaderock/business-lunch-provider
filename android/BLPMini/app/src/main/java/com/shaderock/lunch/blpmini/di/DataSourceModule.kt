package com.shaderock.lunch.blpmini.di

import com.shaderock.lunch.blpmini.data.datasource.network.token.TokenNetworkSource
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserDetailsNetworkSource
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserNetworkSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    fun providesUserNetworkSource(
        retrofitProvider: AuthenticatedRetrofitProvider
    ): UserNetworkSource {
        return retrofitProvider.retrofit.create(UserNetworkSource::class.java)
    }

    @Provides
    fun providesUserDetailsNetworkSource(
        retrofitProvider: AuthenticatedRetrofitProvider
    ): UserDetailsNetworkSource {
        return retrofitProvider.retrofit.create(UserDetailsNetworkSource::class.java)
    }

    @Provides
    fun providesTokenNetworkSource(
        retrofitProvider: AuthenticatedRetrofitProvider
    ): TokenNetworkSource {
        return retrofitProvider.retrofit.create(TokenNetworkSource::class.java)
    }
}