package com.shaderock.backend.auth.login.model;

import com.shaderock.backend.model.type.Role;

import java.util.Set;

public record UserDTO(
        String email,
        String token,
        String firstName,
        String lastName,
        Set<Role> roles) {
}
