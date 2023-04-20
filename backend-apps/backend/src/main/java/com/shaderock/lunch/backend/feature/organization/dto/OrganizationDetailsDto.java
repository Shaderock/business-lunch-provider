package com.shaderock.lunch.backend.feature.organization.dto;

import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.NumberFormat;

/**
 * A DTO for the {@link OrganizationDetails} entity
 */
public record OrganizationDetailsDto(UUID id,
                                     @NotBlank(message = "Name can not be empty")
                                     String name,
                                     @NotBlank(message = "Description can not be empty")
                                     String description,
                                     @Email
                                     String email,
                                     @NumberFormat(pattern = "^\\+(?:[0-9] ?){6,14}[0-9]$")
                                     String phone,
                                     List<UUID> usersIds) implements Serializable {

}
