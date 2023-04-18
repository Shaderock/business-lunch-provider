package com.shaderock.lunch.backend.feature.details.service;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.repository.AppUserDetailsRepository;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

  private final AppUserDetailsRepository appUserDetailsRepository;
  private final AppUserDetailsValidationService appUserDetailsValidationService;

  @Override
  public AppUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return appUserDetailsRepository.findByEmail(username).orElseThrow(
        () -> new UsernameNotFoundException(
            String.format("User(email=[%s]) not found", username)));
  }

  @Transactional
  public Collection<? extends GrantedAuthority> getUserDetailsAuthorities(UserDetails userDetails) {
    return loadUserByUsername(userDetails.getUsername()).getAuthorities();
  }

  @Transactional
  public AppUserDetails create(AppUserDetails userDetails) {
    appUserDetailsValidationService.validateCreate(userDetails);

    LOGGER.info("Creating {}", userDetails);
    AppUserDetails persistedDetails = appUserDetailsRepository.save(userDetails);
    LOGGER.info("Created {}", persistedDetails);

    return persistedDetails;
  }

  public AppUserDetails read(Principal principal) {
    return loadUserByUsername(principal.getName());
  }

  public List<AppUserDetails> read() {
    return appUserDetailsRepository.findAll();
  }
}
