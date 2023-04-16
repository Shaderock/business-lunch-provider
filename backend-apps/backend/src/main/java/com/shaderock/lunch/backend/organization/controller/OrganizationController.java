package com.shaderock.lunch.backend.organization.controller;

import com.shaderock.lunch.backend.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.organization.model.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.utils.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.HashSet;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ORGANIZATION)
public class OrganizationController {

  private final OrganizationDetailsService organizationDetailsService;
  private final OrganizationDetailsMapper organizationDetailsMapper;
  private final AppUserDetailsService userDetailsService;
  private final SupplierService supplierService;

  @GetMapping("/verify-name")
  public ResponseEntity<Boolean> isOrganizationNameValid(@RequestParam @NotNull final String name) {
    return ResponseEntity.ok(!organizationDetailsService.existsByName(name));
  }

  @GetMapping("/verify-email")
  public ResponseEntity<Boolean> isOrganizationEmailValid(
      @RequestParam @NotNull final String email) {
    return ResponseEntity.ok(!organizationDetailsService.existsByEmail(email));
  }

  @GetMapping("/my")
  public ResponseEntity<OrganizationDetailsDto> read(Principal principal) {
    OrganizationDetails organizationDetails = organizationDetailsService.read(principal.getName());
    return ResponseEntity.ok(organizationDetailsMapper.toDto(organizationDetails));
  }

  @GetMapping
  public ResponseEntity<OrganizationDetailsDto> read(@NotNull @RequestParam UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);
    OrganizationDetails organizationDetails = supplier.getOrganizationDetails();

    organizationDetails.setUsers(new HashSet<>());
    organizationDetails.setUsersRequests(new HashSet<>());
    return ResponseEntity.ok(organizationDetailsMapper.toDto(organizationDetails));
  }

  @PutMapping
  public ResponseEntity<OrganizationDetailsDto> update(
      @RequestBody @NonNull @Valid OrganizationDetailsDto organizationDetailsDto,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    OrganizationDetails updated = organizationDetailsService.update(organizationDetailsDto,
        userDetails);

    return ResponseEntity.ok(organizationDetailsMapper.toDto(updated));
  }
}
