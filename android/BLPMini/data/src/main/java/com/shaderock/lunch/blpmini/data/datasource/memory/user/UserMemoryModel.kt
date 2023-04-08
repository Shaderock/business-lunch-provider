package com.shaderock.lunch.blpmini.data.datasource.memory.user

import java.util.*

data class UserMemoryModel(
    val id: UUID,
    val detailsId: UUID,
    val organizationId: UUID?,
    val organizationRequestId: UUID?,
    val preferencesId: UUID?
)
