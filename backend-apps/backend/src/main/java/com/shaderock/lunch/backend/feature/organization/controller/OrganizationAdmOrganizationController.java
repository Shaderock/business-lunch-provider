package com.shaderock.lunch.backend.feature.organization.controller;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.organization.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ORGANIZATION_ADM_ORGANIZATION)
public class OrganizationAdmOrganizationController {

  private final OrganizationDetailsService organizationDetailsService;
  private final OrganizationDetailsMapper organizationDetailsMapper;
  private final AppUserDetailsService userDetailsService;

  @PutMapping
  @Transactional
  public ResponseEntity<OrganizationDetailsDto> update(
      @RequestBody @NonNull @Valid OrganizationDetailsDto organizationDetailsDto,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    OrganizationDetails updated = organizationDetailsService.update(organizationDetailsDto,
        userDetails);

    return ResponseEntity.ok(organizationDetailsMapper.toDto(updated));
  }

  @PutMapping("/logo")
  @Transactional
  public ResponseEntity<Void> uploadImage(@RequestBody @NotNull MultipartFile logo,
      Principal principal) {
    OrganizationDetails organizationDetails = organizationDetailsService.read(principal.getName());
    organizationDetailsService.updateLogo(logo, organizationDetails);

    return ResponseEntity.noContent().build();
  }
}
