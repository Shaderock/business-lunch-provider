package com.shaderock.lunch.backend.feature.config.preference.company.dto;

import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import jakarta.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.UUID;
import org.springframework.format.annotation.NumberFormat;

/**
 * A DTO for the {@link CompanyPreferences} entity
 */
public record CompanyPreferencesDto(UUID id,
                                    UUID companyId,
                                    CompanyDiscountType companyDiscountType,
                                    @NumberFormat(pattern = "^100$|^\\d{0,2}(\\.\\d{1,2})? *%?$")
                                    Integer discountPercentageFirstOrder,
                                    @Min(0)
                                    Double discountFixFirstOrder,
                                    @Min(0)
                                    Double maxDiscountFixFirstOrder,
                                    @Min(0)
                                    Double discountPerDay,
                                    LocalTime deliverAt,
                                    String deliveryAddress) implements Serializable {

}
