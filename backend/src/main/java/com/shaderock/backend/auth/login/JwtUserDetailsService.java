package com.shaderock.backend.auth.login;

import com.shaderock.backend.model.entity.user.AppUser;
import com.shaderock.backend.repository.user.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private final AppUserRepository<AppUser> appUserRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) {
    return appUserRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=[%s} not found", username)));
  }

  @Transactional
  public Collection<? extends GrantedAuthority> getUserDetailsAuthorities(UserDetails userDetails) {
    Optional<AppUser> appUserOptional = appUserRepository.findByEmail(userDetails.getUsername());
    if (appUserOptional.isEmpty()) {
      throw new UsernameNotFoundException(String.format("User with email=[%s} not found", userDetails.getUsername()));
    }
    return appUserOptional.get().getAuthorities();
  }
}
