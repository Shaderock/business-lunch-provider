package com.shaderock.lunch.backend.feature.company.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.feature.company.entity.Company} entity
 */
public record PublicCompanyDto(UUID id,
                               UUID organizationDetailsId,
                               UUID preferencesId) implements Serializable {

}
