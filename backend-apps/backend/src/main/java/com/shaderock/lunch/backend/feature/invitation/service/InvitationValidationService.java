package com.shaderock.lunch.backend.feature.invitation.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import com.shaderock.lunch.backend.feature.invitation.repository.InvitationRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvitationValidationService {

  private final InvitationRepository invitationRepository;

  public void validateCreate(@NonNull Invitation invitation) {
    AppUser appUser = invitation.getAppUser();
    Company company = invitation.getCompany();

    if (company == null) {
      throw new CrudValidationException("Can not create invitation without a company");
    }

    if (appUser == null) {
      throw new CrudValidationException("Can not create invitation without a user");
    }

    if (appUser.getUserDetails().getRoles().size() != 1 ||
        !appUser.getUserDetails().getRoles().contains(Role.USER)) {
      throw new CrudValidationException("User can not be invited to an organization");
    }

    if (invitationRepository.existsByCompanyAndAppUser(company,
        appUser)) {
      throw new CrudValidationException(
          String.format("Invitation for User(email=[%s]) and Company(name=[%s]) already exists",
              appUser.getUserDetails().getEmail(),
              company.getOrganizationDetails().getName()));
    }
  }
}
