package com.shaderock.lunch.backend.feature.invitation.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.invitation.entity.Invitation;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends BaseEntityRepository<Invitation> {

  Optional<Invitation> findByCompanyAndAppUser(Company company, AppUser appUser);

  List<Invitation> findByCompany(Company company);

  List<Invitation> findByAppUser(AppUser appUser);

  boolean existsByCompanyAndAppUser(Company company, AppUser appUser);

}
