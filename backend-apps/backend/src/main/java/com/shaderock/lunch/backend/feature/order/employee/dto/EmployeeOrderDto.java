package com.shaderock.lunch.backend.feature.order.employee.dto;

import com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder;
import com.shaderock.lunch.backend.feature.order.employee.type.EmployeeOrderStatus;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link EmployeeOrder} entity
 */
public record EmployeeOrderDto(UUID id,
                               UUID companyOrderId,
                               double supplierDefaultPrice,
                               double supplierDiscount,
                               double companyDiscount,
                               double finalPrice,
                               EmployeeOrderStatus status,
                               Collection<UUID> optionIds) implements Serializable {

}
