package com.shaderock.lunch.backend.feature.organization.dto;

import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link OrganizationDetails}
 */
public record OrganizationDetailsDto(UUID id,
                                     String name,
                                     String description,
                                     String email,
                                     String phone,
                                     Set<UUID> usersIds,
                                     Set<UUID> usersRequestsIds) implements Serializable {

}
