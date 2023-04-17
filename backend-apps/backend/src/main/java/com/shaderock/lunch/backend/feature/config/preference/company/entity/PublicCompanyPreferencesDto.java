package com.shaderock.lunch.backend.feature.config.preference.company.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link CompanyPreferences} entity
 */
public record PublicCompanyPreferencesDto(UUID id,
                                          UUID companyId,
                                          String deliveryAddress) implements Serializable {

}
