package com.shaderock.lunch.backend.organization.company.controller;

import com.shaderock.lunch.backend.organization.company.CompanyService;
import com.shaderock.lunch.backend.organization.company.mapper.CompanyMapper;
import com.shaderock.lunch.backend.organization.company.model.dto.CompanyDto;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/company")
public class CompanyController {

  private final CompanyService companyService;
  private final CompanyMapper companyMapper;

  @PostMapping("/register")
  public ResponseEntity<CompanyDto> registerCompany(
      @RequestBody @Valid final OrganizationRegistrationForm form, Principal principal) {
    Company registeredCompany = companyService.register(form, principal);
    CompanyDto responseCompany = companyMapper.toDto(registeredCompany);
    return ResponseEntity.ok(responseCompany);
  }

  @GetMapping("/my")
  public ResponseEntity<CompanyDto> getUserCompany(Principal principal) {
    Company userCompany = companyService.readUserCompany(principal);
    CompanyDto userCompanyDto = companyMapper.toDto(userCompany);
    return ResponseEntity.ok(userCompanyDto);
  }
}
