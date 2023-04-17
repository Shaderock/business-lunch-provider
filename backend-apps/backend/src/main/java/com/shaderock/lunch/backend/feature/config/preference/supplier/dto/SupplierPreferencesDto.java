package com.shaderock.lunch.backend.feature.config.preference.supplier.dto;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the
 * {@link
 * SupplierPreferences}
 */
public record SupplierPreferencesDto(UUID id, UUID supplierId,
                                     Duration requestOffset, LocalTime deliveryPeriodStartTime,
                                     LocalTime deliveryPeriodEndTime,
                                     int minimumOrdersPerCompanyRequest,
                                     OrderType orderType,
                                     Set<UUID> priceForCategoriesIds,
                                     UUID orderCapacityId) implements Serializable {

}
