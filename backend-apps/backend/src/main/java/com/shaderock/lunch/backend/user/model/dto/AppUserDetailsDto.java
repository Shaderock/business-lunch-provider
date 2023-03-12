package com.shaderock.lunch.backend.user.model.dto;

import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.user.model.entity.AppUserDetails} entity
 */
@Builder
public record AppUserDetailsDto(UUID id,
                                UUID appUserId,
                                @Email String email,
                                String firstName,
                                String lastName,
                                Set<Role> roles) implements Serializable {

}
