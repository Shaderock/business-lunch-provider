package com.shaderock.lunch.backend.user.model.dto;

import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;
import lombok.Builder;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.user.model.entity.AppUserDetails} entity
 */
@Builder
public record AppUserDetailsDto(Long id,
                                Long appUserId,
                                @Email String email,
                                String firstName,
                                String lastName,
                                Set<Role> roles) implements Serializable {

}
