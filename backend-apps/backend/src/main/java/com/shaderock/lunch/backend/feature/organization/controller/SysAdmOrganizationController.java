package com.shaderock.lunch.backend.feature.organization.controller;

import com.shaderock.lunch.backend.feature.organization.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
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
@RequestMapping(ApiConstants.SYS_ADM_ORGANIZATION)
public class SysAdmOrganizationController {

  private final OrganizationDetailsService organizationDetailsService;
  private final OrganizationDetailsMapper organizationDetailsMapper;

  @GetMapping
  public ResponseEntity<List<OrganizationDetailsDto>> readAll() {
    List<OrganizationDetails> details = organizationDetailsService.read();
    return ResponseEntity.ok(details.stream().map(organizationDetailsMapper::toDto).toList());
  }

}
