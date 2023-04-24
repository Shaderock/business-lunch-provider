package com.shaderock.lunch.backend.feature.invitation.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.invitation.dto.InvitationDto;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import com.shaderock.lunch.backend.feature.invitation.mapper.InvitationMapper;
import com.shaderock.lunch.backend.feature.invitation.service.InvitationService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.COMPANY_ADM_INVITATION)
public class CompanyAdmInvitationController {

  private final InvitationMapper invitationMapper;
  private final InvitationService invitationService;
  private final AppUserDetailsService userDetailsService;
  private final CompanyService companyService;

  @GetMapping("/all")
  @Transactional
  public ResponseEntity<List<InvitationDto>> read(Principal principal) {
    Company company = companyService.read(principal);
    List<Invitation> invitations = company.getInvitations();
    return ResponseEntity.ok(invitations.stream().map(invitationMapper::toDto).toList());
  }

  @PostMapping
  @Transactional
  public ResponseEntity<InvitationDto> invite(@RequestParam @NotNull String userEmail,
      Principal principal) {
    Company company = companyService.read(principal);
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    Invitation invitation = invitationService.create(company, userDetails.getAppUser());
    return ResponseEntity.ok(invitationMapper.toDto(invitation));
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity<Void> dismiss(@RequestParam @NotNull String userEmail,
      Principal principal) {
    Company company = companyService.read(principal);
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    Invitation invitation = invitationService.read(company, userDetails.getAppUser());

    invitationService.delete(invitation,
        Optional.empty(),
        Optional.of(String.format("Invitation for Company (%s) was dismissed",
            company.getOrganizationDetails().getName())));

    return ResponseEntity.noContent().build();
  }
}
