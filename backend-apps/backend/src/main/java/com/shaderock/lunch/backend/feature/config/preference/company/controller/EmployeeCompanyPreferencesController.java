package com.shaderock.lunch.backend.feature.config.preference.company.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.company.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.mapper.CompanyPreferencesMapper;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.EMPLOYEE_COMPANY_PREFERENCES)
public class EmployeeCompanyPreferencesController {

  private final CompanyService companyService;
  private final CompanyPreferencesMapper preferencesMapper;

  @GetMapping
  public ResponseEntity<CompanyPreferencesDto> read(Principal principal) {
    Company company = companyService.read(principal);
    CompanyPreferences preferences = company.getPreferences();
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }

}
