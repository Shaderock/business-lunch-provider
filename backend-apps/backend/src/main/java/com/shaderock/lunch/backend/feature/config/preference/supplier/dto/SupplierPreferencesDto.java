package com.shaderock.lunch.backend.feature.config.preference.supplier.dto;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.CategoryTag;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link SupplierPreferences} entity
 */
public record SupplierPreferencesDto(
    UUID id,
    UUID supplierId,
    Duration requestOffset,
    LocalTime workDayStart,
    LocalTime workDayEnd,
    @NotNull
    @Min(value = 1, message = "minimumOrdersPerCompanyRequest must be greater than 0")
    Integer minimumOrdersPerCompanyRequest,
    @NotNull
    @Min(value = 1, message = "minimumCategoriesForEmployeeOrder must be greater than 0")
    Integer minimumCategoriesForEmployeeOrder,
    OrderType orderType,
    Set<UUID> categoriesPricesIds,
    UUID orderCapacityId,
    List<CategoryTag> categoriesTags) implements Serializable {

}
