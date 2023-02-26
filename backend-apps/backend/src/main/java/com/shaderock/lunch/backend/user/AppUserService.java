package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.user.mapper.AppUserMapper;
import com.shaderock.lunch.backend.user.model.dto.AppUserDto;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.preferences.model.entity.EmployeePreferenceConfig;
import com.shaderock.lunch.backend.user.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.ArrayList;
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

  public EmployeePreferenceConfig getUserProfilePreferences(Principal principal) {
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
      throw new ValidationException("Provided user is [null]");
    }
  }

  public List<AppUserDto> readAllAsDTO() {
    List<AppUser> appUsers = readAll();
    return appUsers.stream().map(appUserMapper::toDto).toList();
  }

  public List<AppUser> readAll() {
    List<AppUser> result = new ArrayList<>();
    for (AppUser appUser : appUserRepository.findAll()) {
      result.add(appUser);
    }

    return result;
  }
}
