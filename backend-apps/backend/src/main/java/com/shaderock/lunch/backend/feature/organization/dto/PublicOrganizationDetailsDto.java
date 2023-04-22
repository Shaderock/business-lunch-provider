package com.shaderock.lunch.backend.feature.organization.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails}
 * entity
 */
public record PublicOrganizationDetailsDto(UUID id,
                                           String name,
                                           String description,
                                           String email,
                                           String phone) implements Serializable {

}
