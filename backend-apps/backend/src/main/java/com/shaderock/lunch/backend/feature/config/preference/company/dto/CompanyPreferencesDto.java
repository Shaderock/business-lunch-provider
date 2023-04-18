package com.shaderock.lunch.backend.feature.config.preference.company.dto;

import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link CompanyPreferences} entity
 */
public record CompanyPreferencesDto(UUID id,
                                    UUID companyId,
                                    CompanyDiscountType companyDiscountType,
                                    Integer discountPercentageFirstOrder,
                                    Double discountFixFirstOrder,
                                    Double maxDiscountFixFirstOrder,
                                    Double discountPerDay,
                                    String deliveryAddress) implements Serializable {

}
