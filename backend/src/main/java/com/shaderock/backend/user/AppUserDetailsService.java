package com.shaderock.backend.user;

import com.shaderock.backend.user.model.entity.AppUserDetails;
import com.shaderock.backend.user.repository.UserDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final UserDetailsRepository userDetailsRepository;

  @Override
  public AppUserDetails loadUserByUsername(final String username) {
    return userDetailsRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=[%s] not found", username)));
  }

  @Transactional
  public Collection<? extends GrantedAuthority> getUserDetailsAuthorities(UserDetails userDetails) {
    return loadUserByUsername(userDetails.getUsername()).getAuthorities();
  }
}
