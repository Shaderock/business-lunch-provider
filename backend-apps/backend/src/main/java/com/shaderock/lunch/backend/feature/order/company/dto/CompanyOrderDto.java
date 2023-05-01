package com.shaderock.lunch.backend.feature.order.company.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shaderock.lunch.backend.feature.order.company.entity.CompanyOrder;
import com.shaderock.lunch.backend.feature.order.company.type.CompanyOrderStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link CompanyOrder} entity
 */
public record CompanyOrderDto(
    UUID id,
    String companyName,
    Set<UUID> employeesOrderIds,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Europe/Chisinau")
    LocalDateTime deliverAt,
    CompanyOrderStatus status) implements Serializable {
}
