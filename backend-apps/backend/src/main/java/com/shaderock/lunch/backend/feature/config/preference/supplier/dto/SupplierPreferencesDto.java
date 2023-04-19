package com.shaderock.lunch.backend.feature.config.preference.supplier.dto;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link SupplierPreferences} entity
 */
public record SupplierPreferencesDto(
    UUID id,
    UUID supplierId,
    @PositiveOrZero(message = "requestOffset cannot be negative")
    Duration requestOffset,
    LocalTime deliveryPeriodStartTime,
    LocalTime deliveryPeriodEndTime,
    @Min(value = 1, message = "minimumOrdersPerCompanyRequest must be greater than 0")
    int minimumOrdersPerCompanyRequest,
    @Min(value = 1, message = "minimumCategoriesForEmployeeOrder must be greater than 0")
    int minimumCategoriesForEmployeeOrder,
    OrderType orderType,
    Set<UUID> pricesForCategoriesIds,
    UUID orderCapacityId) implements Serializable {

}
