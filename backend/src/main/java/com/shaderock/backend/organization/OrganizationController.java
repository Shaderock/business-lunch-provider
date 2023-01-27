package com.shaderock.backend.organization;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/organization")
public class OrganizationController {
  private final OrganizationService organizationService;

  @GetMapping("/verify-name")
  public ResponseEntity<Boolean> isOrganizationNameValid(@RequestParam @NotNull final String name) {
    return ResponseEntity.ok(!organizationService.doesOrganizationExistByName(name));
  }

  @GetMapping("/verify-email")
  public ResponseEntity<Boolean> isOrganizationEmailValid(@RequestParam @NotNull final String email) {
    return ResponseEntity.ok(!organizationService.doesOrganizationExistByEmail(email));
  }
}
