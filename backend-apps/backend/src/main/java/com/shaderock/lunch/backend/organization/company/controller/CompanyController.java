package com.shaderock.lunch.backend.organization.company.controller;

import com.shaderock.lunch.backend.organization.company.mapper.CompanyMapper;
import com.shaderock.lunch.backend.organization.company.model.dto.CompanyDto;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import com.shaderock.lunch.backend.organization.company.service.CompanyService;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.utils.ApiConstants;
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
@RequestMapping(ApiConstants.COMPANY)
public class CompanyController {

  private final CompanyService companyService;
  private final CompanyMapper companyMapper;
  private final AppUserDetailsService userDetailsService;

  @PostMapping("/register")
  public ResponseEntity<CompanyDto> registerCompany(
      @RequestBody @Valid final OrganizationRegistrationForm form, Principal principal) {
    // todo think of @AuthenticationPrincipal AppUserDetails userDetails
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Company registeredCompany = companyService.register(form, userDetails);
    CompanyDto responseCompany = companyMapper.toDto(registeredCompany);
    return ResponseEntity.ok(responseCompany);
  }

  @GetMapping("/my")
  public ResponseEntity<CompanyDto> read(Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Company userCompany = companyService.read(userDetails);
    CompanyDto userCompanyDto = companyMapper.toDto(userCompany);
    return ResponseEntity.ok(userCompanyDto);
  }
}
