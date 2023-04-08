package com.shaderock.lunch.blpmini.data.repository

import com.shaderock.lunch.blpmini.data.datasource.memory.user.UserMemoryModel
import com.shaderock.lunch.blpmini.data.datasource.memory.user.UserMemorySource
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserNetworkModel
import com.shaderock.lunch.blpmini.data.datasource.network.user.UserNetworkSource
import com.shaderock.lunch.blpmini.domain.entity.UserEntity
import com.shaderock.lunch.blpmini.domain.gateway.UserGateway
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userMemorySource: UserMemorySource,
    private val userNetworkSource: UserNetworkSource
) : UserGateway {
    override fun getUser() = flow {
        emit(getFromMemory() ?: getFromNetwork())
    }

    private fun getFromMemory(): UserEntity? = userMemorySource.data?.toEntity()

    private suspend fun getFromNetwork(): UserEntity {
        val data = userNetworkSource.getUser()
        userMemorySource.data = data.toMemory()
        return data.toEntity()
    }
}

private fun UserMemoryModel.toEntity(): UserEntity {
    return UserEntity(id, detailsId, organizationId, organizationRequestId, preferencesId)
}

private fun UserNetworkModel.toMemory(): UserMemoryModel {
    return UserMemoryModel(id, detailsId, organizationId, organizationRequestId, preferencesId)
}

private fun UserNetworkModel.toEntity(): UserEntity {
    return UserEntity(id, detailsId, organizationId, organizationRequestId, preferencesId)
}