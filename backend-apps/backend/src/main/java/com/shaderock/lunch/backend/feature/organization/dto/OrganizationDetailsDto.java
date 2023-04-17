package com.shaderock.lunch.backend.feature.organization.dto;

import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link OrganizationDetails} entity
 */
public record OrganizationDetailsDto(UUID id,
                                     String name,
                                     String description,
                                     String email,
                                     String phone,
                                     List<UUID> usersIds,
                                     List<UUID> usersRequestsIds) implements Serializable {

}
