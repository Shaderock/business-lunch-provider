package com.shaderock.lunch.backend.organization.company;

import com.shaderock.lunch.backend.organization.company.model.CompanyDTO;
import com.shaderock.lunch.backend.organization.company.model.CompanyRegistrationForm;
import com.shaderock.lunch.backend.organization.company.model.entity.Company;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/company")
public class CompanyController {
  private final CompanyService companyService;
  private final CompanyDTOConverter companyDTOConverter;

  @PostMapping("/register")
  public ResponseEntity<CompanyDTO> registerCompany(@RequestBody @Valid final CompanyRegistrationForm companyRegistrationForm,
                                                    Principal principal) {
    Company registeredCompany = companyService.register(companyRegistrationForm, principal);
    CompanyDTO responseCompany = companyDTOConverter.convertToDto(registeredCompany);
    return ResponseEntity.ok(responseCompany);
  }

  @GetMapping("/my")
  public ResponseEntity<CompanyDTO> getUserCompany(Principal principal) {
    Company userCompany = companyService.getUserCompany(principal);
    CompanyDTO userCompanyDto = companyDTOConverter.convertToDto(userCompany);
    return ResponseEntity.ok(userCompanyDto);
  }
}
