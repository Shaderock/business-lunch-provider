package com.shaderock.lunch.blpmini.data.datasource.memory.user

import com.shaderock.lunch.blpmini.domain.entity.Role
import java.util.*

data class UserDetailsMemoryModel(
    val id: UUID,
    val appUserId: UUID,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<Role>
)
