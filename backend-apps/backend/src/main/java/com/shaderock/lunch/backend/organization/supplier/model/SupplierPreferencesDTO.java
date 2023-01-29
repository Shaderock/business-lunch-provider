package com.shaderock.lunch.backend.organization.supplier.model;

import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.OrderCapacity;
import com.shaderock.lunch.backend.organization.supplier.preference.model.type.OrderType;
import com.shaderock.lunch.backend.organization.supplier.preference.price.model.entity.PriceBy;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

public record SupplierPreferencesDTO(
        Duration requestOffset,
        LocalTime deliveryPeriodStartTime,
        LocalTime deliveryPeriodEndTime,
        int minimumOrdersPerRequest,
        OrderType orderType,
        Set<PriceBy> prices, // todo
        OrderCapacity orderCapacity //todo
) {
}
