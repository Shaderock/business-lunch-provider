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
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
@RequestMapping(ApiConstants.INVITATION)
public class InvitationController {

  private final AppUserDetailsService userDetailsService;
  private final InvitationService invitationService;
  private final CompanyService companyService;
  private final InvitationMapper invitationMapper;

  @GetMapping("/all")
  public ResponseEntity<List<InvitationDto>> read(Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    List<Invitation> invitations = userDetails.getAppUser().getInvitations();

    return ResponseEntity.ok(invitations.stream().map(invitationMapper::toDto).toList());
  }

  @PostMapping
  public ResponseEntity<Void> accept(@RequestParam @NotNull UUID companyId, Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    Company company = companyService.read(companyId);

    Invitation invitation = invitationService.read(company, userDetails.getAppUser());
    companyService.addEmployee(userDetails.getAppUser(), company);

    invitationService.delete(invitation,
        Optional.of(String.format("Invitation for User (%s) was accepted", userDetails.getEmail())),
        Optional.empty());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> decline(@RequestParam @NotNull UUID companyId, Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    Company company = companyService.read(companyId);

    Invitation invitation = invitationService.read(company, userDetails.getAppUser());
    invitationService.delete(invitation, Optional.of(
            String.format("Invitation for User (%s) was declined", userDetails.getEmail())),
        Optional.empty());
    return ResponseEntity.noContent().build();
  }
}
