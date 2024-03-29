package com.shaderock.lunch.backend.feature.company.dto;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link Company} entity
 */
public record CompanyDto(UUID id,
                         UUID organizationDetailsId,
                         Set<UUID> subscriptionsIds,
                         UUID preferencesId) implements Serializable {

}
