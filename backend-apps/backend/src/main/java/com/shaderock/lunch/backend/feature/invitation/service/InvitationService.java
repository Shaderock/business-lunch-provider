package com.shaderock.lunch.backend.feature.invitation.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import com.shaderock.lunch.backend.feature.invitation.repository.InvitationRepository;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.notification.service.NotificationService;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvitationService {

  private final InvitationRepository invitationRepository;
  private final NotificationService notificationService;
  private final InvitationValidationService invitationValidationService;

  @Transactional
  public Invitation create(Invitation invitation) {
    invitationValidationService.validateCreate(invitation);
    return invitationRepository.save(invitation);
  }

  public Invitation create(Company company, AppUser appUser) {
    Invitation invitation = Invitation.builder()
        .company(company)
        .appUser(appUser)
        .build();

    return create(invitation);
  }

  public Invitation read(UUID id) {
    return invitationRepository.findById(id).orElseThrow(
        () -> new CrudValidationException(String.format("Invitation(id=[%s]) not found", id)));
  }

  public List<Invitation> read(Company company) {
    return invitationRepository.findByCompany(company);
  }

  public List<Invitation> read(AppUser appUser) {
    return invitationRepository.findByAppUser(appUser);
  }

  public Invitation read(Company company, AppUser appUser) {
    return invitationRepository.findByCompanyAndAppUser(company, appUser).orElseThrow(() ->
        new CrudValidationException(
            String.format("Invitation for Company(name=[%s]) and User(email=[%s]) not found",
                company.getOrganizationDetails().getName(), appUser.getUserDetails().getEmail())));
  }

  @Transactional
  public void delete(Invitation invitation, Optional<String> companyAdminsNotificationContent,
      Optional<String> userNotificationContent) {
    OrganizationDetails organizationDetails = invitation.getCompany().getOrganizationDetails();
    AppUserDetails userDetails = invitation.getAppUser().getUserDetails();

    companyAdminsNotificationContent.ifPresent(content -> {
      Notification companyNotification = Notification.builder()
          .content(content)
          .build();

      notificationService.create(companyNotification,
          organizationDetails.getUsers().stream().map(AppUser::getUserDetails).toList(),
          true,
          Set.of(Role.COMPANY_ADMIN));
    });

    userNotificationContent.ifPresent(content -> {
      Notification userNotification = Notification.builder()
          .content(content)
          .build();

      notificationService.create(userNotification, List.of(userDetails), true);
    });

    invitationRepository.delete(invitation);
  }

  public void delete(@NonNull AppUser appUser, @NonNull String companyAdminsNotificationContent) {
    for (Invitation invitation : appUser.getInvitations()) {
      delete(invitation, Optional.of(companyAdminsNotificationContent), Optional.empty());
    }
  }
}
