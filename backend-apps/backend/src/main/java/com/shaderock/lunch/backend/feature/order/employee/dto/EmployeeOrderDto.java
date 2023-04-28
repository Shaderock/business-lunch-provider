package com.shaderock.lunch.backend.feature.order.employee.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.service.TwoDigitsAfterPointSerializer;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link EmployeeOrder} entity
 */
public record EmployeeOrderDto(
    UUID id,
    UUID companyOrderId,
    @JsonSerialize(using = TwoDigitsAfterPointSerializer.class) double supplierDefaultPrice,
    @JsonSerialize(using = TwoDigitsAfterPointSerializer.class) double supplierDiscount,
    @JsonSerialize(using = TwoDigitsAfterPointSerializer.class) double companyDiscount,
    @JsonSerialize(using = TwoDigitsAfterPointSerializer.class) double finalPrice,
    EmployeeOrderStatus status,
    LocalDate orderDate,
    Collection<UUID> optionIds) implements Serializable {

}
