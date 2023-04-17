package com.shaderock.lunch.backend.feature.user.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import com.shaderock.lunch.backend.feature.user.dto.AppUserDto;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.mapper.AppUserMapper;
import com.shaderock.lunch.backend.feature.user.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {

  private final AppUserDetailsService appUserDetailsService;
  private final AppUserRepository appUserRepository;
  private final AppUserMapper appUserMapper;

  public AppUser readProfile(Principal principal) {
    AppUserDetails userDetails = appUserDetailsService.readProfile(principal);
    return userDetails.getAppUser();
  }

  public EmployeePreferences getUserProfilePreferences(Principal principal) {
    return readProfile(principal).getPreferences();
  }

  @Transactional
  public AppUser create(AppUser appUser) {
    validateCreate(appUser);

    LOGGER.info("Creating {}", appUser);
    AppUserDetails persistedDetails = appUserDetailsService.create(appUser.getUserDetails());
    appUser.setUserDetails(persistedDetails);
    AppUser persistedUser = appUserRepository.save(appUser);
    persistedDetails.setAppUser(persistedUser);
    LOGGER.info("Created {}", appUser);

    return persistedUser;
  }

  private void validateCreate(AppUser appUser) {
    if (Objects.isNull(appUser)) {
      throw new CrudValidationException("Provided user is [null]");
    }
  }

  public List<AppUserDto> readAllAsDTO() {
    List<AppUser> appUsers = readAll();
    return appUsers.stream().map(appUserMapper::toDto).toList();
  }

  public List<AppUser> readAll() {
    return appUserRepository.findAll();
  }
}
