package com.shaderock.lunch.backend.feature.config.preference.company.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.company.dto.CompanyPreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.mapper.CompanyPreferencesMapper;
import com.shaderock.lunch.backend.feature.config.preference.company.service.CompanyPreferencesService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.COMPANY_ADM_COMPANY_PREFERENCES)
public class CompanyAdmPreferencesController {

  private final CompanyPreferencesMapper preferencesMapper;
  private final CompanyService companyService;
  private final CompanyPreferencesService companyPreferencesService;
  private final AppUserDetailsService userDetailsService;

  @GetMapping
  public ResponseEntity<CompanyPreferencesDto> read(Principal principal) {
    Company company = companyService.read(principal);
    CompanyPreferences preferences = company.getPreferences();
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }

  @PutMapping
  public ResponseEntity<CompanyPreferencesDto> update(Principal principal,
      @NotNull @Valid @RequestBody CompanyPreferencesDto companyPreferencesDto) {
    AppUserDetails userDetails = userDetailsService.read(principal);

    CompanyPreferences preferences = companyPreferencesService.update(companyPreferencesDto,
        userDetails);
    return ResponseEntity.ok(preferencesMapper.toDto(preferences));
  }
}
