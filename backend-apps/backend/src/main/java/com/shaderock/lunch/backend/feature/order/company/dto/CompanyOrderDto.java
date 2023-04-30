package com.shaderock.lunch.backend.feature.order.company.dto;

import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.company.type.CompanyOrderStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * A DTO for the {@link CompanyOrder} entity
 */
public record CompanyOrderDto(
    UUID id,
    Set<UUID> employeesOrderIds,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime deliverAt,
    CompanyOrderStatus status) implements Serializable {

}
