package com.shaderock.lunch.blpmini.data.repository

import com.shaderock.lunch.blpmini.data.datasource.memory.user.UserDetailsMemoryModel
import com.shaderock.lunch.blpmini.data.datasource.memory.user.UserDetailsMemorySource
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserDetailsNetworkModel
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserDetailsNetworkSource
import com.shaderock.lunch.blpmini.domain.entity.UserDetailsEntity
import com.shaderock.lunch.blpmini.domain.gateway.UserDetailsGateway
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(
    private val userDetailsMemorySource: UserDetailsMemorySource,
    private val userDetailsNetworkSource: UserDetailsNetworkSource
) : UserDetailsGateway {
    override fun getUserDetails() = flow {
        emit(getFromMemory() ?: getFromNetwork())
    }

    private fun getFromMemory(): UserDetailsEntity? = userDetailsMemorySource.data?.toEntity()

    private suspend fun getFromNetwork(): UserDetailsEntity {
        val data = userDetailsNetworkSource.getUserDetails()
        userDetailsMemorySource.data = data.toMemory()
        return data.toEntity()
    }
}

private fun UserDetailsMemoryModel.toEntity(): UserDetailsEntity {
    return UserDetailsEntity(id, appUserId, email, firstName, lastName, roles)
}

private fun UserDetailsNetworkModel.toMemory(): UserDetailsMemoryModel {
    return UserDetailsMemoryModel(id, appUserId, email, firstName, lastName, roles)
}

private fun UserDetailsNetworkModel.toEntity(): UserDetailsEntity {
    return UserDetailsEntity(id, appUserId, email, firstName, lastName, roles)
}