package com.shaderock.lunch.blpmini.di

import com.google.gson.Gson
import com.shaderock.lunch.blpmini.data.datasource.network.NetworkEndpoint
import com.shaderock.lunch.blpmini.data.datasource.network.token.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class OkHttpClientFactory @Inject constructor() {
    fun build(vararg interceptors: Interceptor): OkHttpClient {
        val builder = newClientBuilder()
        interceptors.forEach(builder::addInterceptor)
        return builder
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private fun newClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }
}

class AuthenticatedRetrofitProvider @Inject constructor(
    retrofitClientBuilder: Retrofit.Builder,
    okHttpClientFactory: OkHttpClientFactory,
    authInterceptor: AuthInterceptor
) {
    val retrofit: Retrofit = retrofitClientBuilder
        .client(okHttpClientFactory.build(authInterceptor))
        .build()
}

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun providesRetrofitClientBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(NetworkEndpoint.BASE)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    fun providesGson(): Gson = Gson()
}