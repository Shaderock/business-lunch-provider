package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.user.mapper.AppUserDetailsMapper;
import com.shaderock.lunch.backend.user.model.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.repository.AppUserDetailsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

  private void validateCreate(AppUserDetails details) {
    if (Objects.isNull(details)) {
      throw new ValidationException("Provided user details are [null]");
    }

    if (appUserDetailsRepository.findByEmail(details.getEmail()).isPresent()) {
      throw new ValidationException(String.format("%s already exists", details));
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
    List<AppUserDetails> result = new ArrayList<>();
    for (AppUserDetails appUserDetails : appUserDetailsRepository.findAll()) {
      result.add(appUserDetails);
    }

    return result;
  }
}
