package com.shaderock.lunch.backend.feature.user.dto;

import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.type.Role;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

/**
 * A DTO for the {@link AppUserDetails} entity
 */
@Builder
public record AppUserDetailsDto(UUID id,
                                UUID appUserId,
                                @Email String email,
                                String firstName,
                                String lastName,
                                Set<Role> roles) implements Serializable {

}
