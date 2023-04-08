package com.shaderock.lunch.blpmini.domain.entity

import java.util.*

data class UserEntity(
    val id: UUID,
    val detailsId: UUID,
    val organizationId: UUID?,
    val organizationRequestId: UUID?,
    val preferencesId: UUID?
)
