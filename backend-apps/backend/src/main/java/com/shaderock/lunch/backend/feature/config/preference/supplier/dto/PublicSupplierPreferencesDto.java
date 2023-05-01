package com.shaderock.lunch.backend.feature.config.preference.supplier.dto;

import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.CategoryTag;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link SupplierPreferences} entity
 */
public record PublicSupplierPreferencesDto(
    UUID id,
    Duration requestOffset,
    LocalTime workDayStart,
    LocalTime workDayEnd,
    Integer minimumOrdersPerCompanyRequest,
    Integer minimumCategoriesForEmployeeOrder,
    Set<UUID> categoriesPricesIds,
    OrderType orderType,
    List<CategoryTag> categoriesTags) implements Serializable {

}
