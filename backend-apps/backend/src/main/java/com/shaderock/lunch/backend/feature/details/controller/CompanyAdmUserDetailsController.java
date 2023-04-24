package com.shaderock.lunch.backend.feature.details.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.details.mapper.AppUserDetailsMapper;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.COMPANY_ADM_USER_DETAILS)
public class CompanyAdmUserDetailsController {

  private final CompanyService companyService;
  private final AppUserDetailsMapper userDetailsMapper;

  @GetMapping("/all")
  @Transactional
  public ResponseEntity<List<AppUserDetailsDto>> read(Principal principal) {
    Company company = companyService.read(principal);

    return ResponseEntity.ok(company.getOrganizationDetails().getUsers().stream()
        .map(AppUser::getUserDetails)
        .map(userDetailsMapper::toDto)
        .toList());
  }
}
