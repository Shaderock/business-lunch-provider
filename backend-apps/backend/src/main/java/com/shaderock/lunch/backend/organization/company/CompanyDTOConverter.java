package com.shaderock.lunch.backend.organization.company;

import com.shaderock.lunch.backend.organization.company.model.CompanyDTO;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyDTOConverter {
  @Transactional
  public CompanyDTO convertToDto(Company company) {
    return new CompanyDTO(company.getName(), company.getEmail(), company.getPhone());
  }
}
