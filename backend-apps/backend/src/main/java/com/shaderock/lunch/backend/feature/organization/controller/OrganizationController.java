package com.shaderock.lunch.backend.feature.organization.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import com.shaderock.lunch.backend.feature.invitation.service.InvitationService;
import com.shaderock.lunch.backend.feature.organization.dto.OrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.dto.PublicOrganizationDetailsDto;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.mapper.OrganizationDetailsMapper;
import com.shaderock.lunch.backend.feature.organization.service.OrganizationDetailsService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import com.shaderock.lunch.backend.util.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final InvitationService invitationService;
  private final ImageService imageService;
  private final CompanyService companyService;

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
  @Transactional
  public ResponseEntity<OrganizationDetailsDto> read(Principal principal) {
    OrganizationDetails organizationDetails = organizationDetailsService.read(principal.getName());
    return ResponseEntity.ok(organizationDetailsMapper.toDto(organizationDetails));
  }

  @GetMapping
  @Transactional
  public ResponseEntity<PublicOrganizationDetailsDto> read(@NotNull @RequestParam UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);
    OrganizationDetails organizationDetails = supplier.getOrganizationDetails();

    return ResponseEntity.ok(organizationDetailsMapper.toPublicDto(organizationDetails));
  }

  @GetMapping("/invitation/all")
  @Transactional
  public ResponseEntity<List<PublicOrganizationDetailsDto>> readLogoOfInvited(Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    List<Invitation> invitations = invitationService.read(userDetails.getAppUser());
    List<OrganizationDetails> organizationDetailsList = invitations.stream()
        .map(Invitation::getCompany)
        .map(Company::getOrganizationDetails)
        .toList();

    return ResponseEntity.ok(
        organizationDetailsList.stream().map(organizationDetailsMapper::toPublicDto).toList());
  }

  @GetMapping(value = "/invitation/logo", produces = MediaType.IMAGE_JPEG_VALUE)
  @Transactional
  public byte[] readLogoOfInvited(
      @RequestParam @NotNull UUID companyId, @RequestParam @NotNull boolean isThumbnail,
      Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);

    OrganizationDetails organizationDetails = organizationDetailsService.read(companyId);
    Company company = companyService.read(organizationDetails);

    Invitation invitation = invitationService.read(company, userDetails.getAppUser());
    byte[] logo = invitation.getCompany().getOrganizationDetails().getLogo();

    if (isThumbnail && logo != null) {
      return imageService.resizeToThumbnail(logo);
    }

    return logo;
  }

  @SneakyThrows
  @GetMapping(value = "/my/logo", produces = MediaType.IMAGE_JPEG_VALUE)
  @Transactional
  public byte[] readMenuImage(Principal principal, @RequestParam @NotNull boolean isThumbnail) {
    OrganizationDetails organizationDetails = organizationDetailsService.read(principal.getName());
    byte[] logo = organizationDetails.getLogo();
    if (logo != null && isThumbnail) {
      logo = imageService.resizeToThumbnail(logo);
    }
    return logo;
  }
}
