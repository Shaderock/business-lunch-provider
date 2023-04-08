package com.shaderock.lunch.blpmini.domain.entity

enum class Role(val roleName: String) {
    USER("USER"),
    EMPLOYEE("EMPLOYEE"),
    COMPANY_ADMIN("COMPANY_ADMIN"),
    SUPPLIER("SUPPLIER"),
    SYSTEM_ADMIN("SYSTEM_ADMIN");
}