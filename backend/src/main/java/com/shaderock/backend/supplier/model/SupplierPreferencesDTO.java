package com.shaderock.backend.supplier.model;

import com.shaderock.backend.model.entity.preference.capacity.OrderCapacity;
import com.shaderock.backend.model.entity.preference.price.PriceBy;
import com.shaderock.backend.model.type.OrderType;

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
