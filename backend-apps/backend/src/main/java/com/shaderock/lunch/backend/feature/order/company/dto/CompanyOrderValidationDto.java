package com.shaderock.lunch.backend.feature.order.company.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyOrderValidationDto {

  private boolean valid;
  private List<String> errors;
}
