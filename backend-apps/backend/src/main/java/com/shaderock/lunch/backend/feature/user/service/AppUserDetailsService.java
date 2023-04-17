package com.shaderock.lunch.backend.feature.user.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.user.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.user.mapper.AppUserDetailsMapper;
import com.shaderock.lunch.backend.feature.user.repository.AppUserDetailsRepository;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
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
  private final AppUserDetailsMapper appUserDetailsMapper;

  @Override
  public AppUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return appUserDetailsRepository.findByEmail(username).orElseThrow(
        () -> new UsernameNotFoundException(
            String.format("User with email=[%s] not found", username)));
  }

  @Transactional
  public Collection<? extends GrantedAuthority> getUserDetailsAuthorities(UserDetails userDetails) {
    return loadUserByUsername(userDetails.getUsername()).getAuthorities();
  }

  @Transactional
  public AppUserDetails create(AppUserDetails userDetails) {
    validateCreate(userDetails);

    LOGGER.info("Creating {}", userDetails);
    AppUserDetails persistedDetails = appUserDetailsRepository.save(userDetails);
    LOGGER.info("Created {}", persistedDetails);

    return persistedDetails;
  }

  private void validateCreate(@NonNull AppUserDetails details) {
    if (appUserDetailsRepository.findByEmail(details.getEmail()).isPresent()) {
      throw new CrudValidationException(String.format("%s already exists", details));
    }
  }

  public AppUserDetails readProfile(Principal principal) {
    return loadUserByUsername(principal.getName());
  }

  public List<AppUserDetailsDto> readAllAsDto() {
    List<AppUserDetails> appUsers = readAll();
    return appUsers.stream().map(appUserDetailsMapper::toDto).toList();
  }

  public List<AppUserDetails> readAll() {
    return appUserDetailsRepository.findAll();
  }
}
