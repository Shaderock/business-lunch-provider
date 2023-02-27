package com.shaderock.lunch.backend.organization.controller;

import com.shaderock.lunch.backend.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.organization.model.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
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
@RequestMapping("/api/sysadm/organization/details")
public class PrivilegedOrganizationController {

  private final OrganizationDetailsService organizationDetailsService;
  private final OrganizationDetailsMapper organizationDetailsMapper;

  @GetMapping
  public ResponseEntity<List<OrganizationDetailsDto>> readAll() {
    List<OrganizationDetails> details = organizationDetailsService.readAll();
    List<OrganizationDetails> deletedDetails = organizationDetailsService.readAllDeleted();
    details.addAll(deletedDetails);
    return ResponseEntity.ok(details.stream().map(organizationDetailsMapper::toDto).toList());
  }

}
