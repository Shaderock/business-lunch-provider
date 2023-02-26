package com.shaderock.lunch.backend.organization.company.controller;

import com.shaderock.lunch.backend.organization.company.CompanyService;
import com.shaderock.lunch.backend.organization.company.mapper.CompanyMapper;
import com.shaderock.lunch.backend.organization.company.model.dto.CompanyDto;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
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
@RequestMapping("/api/sysadm/company")
public class PrivilegedCompanyController {

  private final CompanyService companyService;
  private final CompanyMapper companyMapper;

  @GetMapping
  public ResponseEntity<List<CompanyDto>> readAll() {
    List<Company> companies = companyService.readAll(true);
    return ResponseEntity.ok(companies.stream().map(companyMapper::toDto).toList());
  }
}
