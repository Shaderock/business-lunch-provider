package com.shaderock.backend.company;

import com.shaderock.backend.company.model.CompanyDTO;
import com.shaderock.backend.company.model.CompanyRegistrationForm;
import com.shaderock.backend.model.entity.company.Company;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {
  private final CompanyService companyService;

  @PostMapping("/register")
  public ResponseEntity<CompanyDTO> registerCompany(@RequestBody @Valid final CompanyRegistrationForm companyRegistrationForm,
                                                    Principal principal) {
    Company registeredCompany = companyService.register(companyRegistrationForm, principal);
    CompanyDTO responseCompany = companyService.convertToDto(registeredCompany);
    return ResponseEntity.ok(responseCompany);
  }
}
