package com.shaderock.lunch.backend.organization.company.model.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.organization.company.model.entity.Company}
 * entity
 */
public record CompanyDto(UUID id,
                         UUID organizationDetailsId,
                         Set<UUID> subscriptionsIds,
                         Set<UUID> subscriptionsRequestsIds,
                         UUID preferencesId) implements Serializable {

}
