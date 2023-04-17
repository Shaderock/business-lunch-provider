package com.shaderock.lunch.backend.feature.company.controller;

import com.shaderock.lunch.backend.feature.company.dto.CompanyDto;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.mapper.CompanyMapper;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SYS_ADM_COMPANY)
public class PrivilegedCompanyController {

  private final CompanyService companyService;
  private final CompanyMapper companyMapper;

  @GetMapping
  public ResponseEntity<List<CompanyDto>> readAll() {
    List<Company> companies = companyService.readAll();
    return ResponseEntity.ok(companies.stream().map(companyMapper::toDto).toList());
  }
}
