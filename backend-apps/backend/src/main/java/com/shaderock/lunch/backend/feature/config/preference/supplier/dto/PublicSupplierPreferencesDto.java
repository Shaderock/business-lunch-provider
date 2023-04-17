package com.shaderock.lunch.backend.feature.config.preference.supplier.dto;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

/**
 * A DTO for the {@link SupplierPreferences} entity
 */
public record PublicSupplierPreferencesDto(UUID id, Duration requestOffset,
                                           LocalTime deliveryPeriodStartTime,
                                           LocalTime deliveryPeriodEndTime,
                                           int minimumOrdersPerRequest,
                                           OrderType orderType) implements Serializable {

}
