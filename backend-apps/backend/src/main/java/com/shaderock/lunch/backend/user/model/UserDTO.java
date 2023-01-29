package com.shaderock.lunch.backend.user.model;

import com.shaderock.lunch.backend.user.model.type.Role;

import java.util.Set;

public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        Set<Role> roles) {
}
