package com.shaderock.lunch.backend.organization.company.preference.model.dto;

import com.shaderock.lunch.backend.organization.company.preference.model.entity.CompanyPreferences;
import com.shaderock.lunch.backend.organization.company.preference.model.type.CompanyDiscountType;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link CompanyPreferences} BaseEntity
 */
public record CompanyPreferencesDto(UUID id, UUID companyId,
                                    CompanyDiscountType companyDiscountType,
                                    String deliveryAddress) implements Serializable {

}
