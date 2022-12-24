package com.shaderock.backend.auth.login;

import com.shaderock.backend.model.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private final AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) {
    return appUserRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=[%s} not found", username)));
  }

}
