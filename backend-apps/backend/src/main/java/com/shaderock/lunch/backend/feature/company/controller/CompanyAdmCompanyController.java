package com.shaderock.lunch.backend.feature.company.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.COMPANY_ADM_COMPANY)
public class CompanyAdmCompanyController {

  private final CompanyService companyService;
  private final AppUserDetailsService userDetailsService;

  @PostMapping("/remove-user")
  public ResponseEntity<Void> removeUser(@RequestParam @NotNull String userEmail,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    Company company = companyService.read(principal);
    companyService.removeEmployee(userDetails.getAppUser(), company);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/grant-company-admin-rights")
  public ResponseEntity<Void> grantAdminRole(@RequestParam @NotNull String userEmail,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    Company company = companyService.read(principal);
    companyService.grantAdminRole(userDetails.getAppUser(), company);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/revoke-company-admin-rights")
  public ResponseEntity<Void> revokeAdminRole(@RequestParam @NotNull String userEmail,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    Company company = companyService.read(principal);
    companyService.revokeAdminRole(userDetails.getAppUser(), company);
    return ResponseEntity.noContent().build();
  }
}
