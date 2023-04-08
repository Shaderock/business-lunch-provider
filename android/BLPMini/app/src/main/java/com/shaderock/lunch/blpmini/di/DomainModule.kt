package com.shaderock.lunch.blpmini.di

import com.shaderock.lunch.blpmini.domain.gateway.AuthGateway
import com.shaderock.lunch.blpmini.domain.gateway.UserDetailsGateway
import com.shaderock.lunch.blpmini.domain.gateway.UserGateway
import com.shaderock.lunch.blpmini.domain.usecase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun providesAuthUseCase(
        authGateway: AuthGateway,
        userGateway: UserGateway,
        userDetailsGateway: UserDetailsGateway
    ): AuthUseCase {
        return AuthUseCase(authGateway, userGateway, userDetailsGateway)
    }
}