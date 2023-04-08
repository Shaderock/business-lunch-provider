package com.shaderock.lunch.blpmini.domain.entity

import java.util.*

data class UserDetailsEntity(
    val id: UUID,
    val appUserId: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<Role>
)
