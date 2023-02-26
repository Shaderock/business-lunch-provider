package com.shaderock.lunch.backend.organization.model.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails}
 * entity
 */
public record OrganizationDetailsDto(UUID id,
                                     String name,
                                     String description,
                                     String email,
                                     String phone,
                                     Set<Long> usersIds,
                                     Set<Long> usersRequestsIds) implements Serializable {

}
