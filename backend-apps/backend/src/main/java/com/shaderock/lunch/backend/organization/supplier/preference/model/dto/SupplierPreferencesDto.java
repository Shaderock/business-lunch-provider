package com.shaderock.lunch.backend.organization.supplier.preference.model.dto;

import com.shaderock.lunch.backend.organization.supplier.preference.model.type.OrderType;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.PriceByType;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the
 * {@link
 * com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences}
 * entity
 */
public record SupplierPreferencesDto(UUID id, UUID supplierId,
                                     Duration requestOffset, LocalTime deliveryPeriodStartTime,
                                     LocalTime deliveryPeriodEndTime, int minimumOrdersPerRequest,
                                     OrderType orderType, PriceByType priceByType,
                                     Set<UUID> priceForCategoriesIds,
                                     UUID orderCapacityId) implements Serializable {

}
