package com.shaderock.lunch.backend.feature.order.employee.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * A DTO for the {@link com.shaderock.lunch.backend.feature.order.employee.entity.EmployeeOrder}
 * entity
 */
@Data
@Builder
public class EmployeeOrderValidationDto implements Serializable {

  private final boolean valid;
  private final List<String> errors;
  private final UUID supplierId;
  private final UUID userDetailsId;
  private final UUID orderId;
}
