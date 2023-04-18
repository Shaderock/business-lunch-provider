package com.shaderock.lunch.backend.feature.user.service;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.feature.user.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {

  private final AppUserDetailsService userDetailsService;
  private final AppUserRepository userRepository;

  @Transactional
  public AppUser create(@NonNull AppUser appUser) {
    LOGGER.info("Creating {}", appUser);
    AppUserDetails persistedDetails = userDetailsService.create(appUser.getUserDetails());
    appUser.setUserDetails(persistedDetails);
    AppUser persistedUser = userRepository.save(appUser);
    persistedDetails.setAppUser(persistedUser);
    LOGGER.info("Created {}", appUser);

    return persistedUser;
  }

  public AppUser read(Principal principal) {
    AppUserDetails userDetails = userDetailsService.read(principal);
    return userDetails.getAppUser();
  }

  public List<AppUser> read() {
    return userRepository.findAll();
  }
}
