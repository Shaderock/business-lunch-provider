package com.shaderock.backend.auth;

import com.shaderock.backend.auth.login.JwtTokenService;
import com.shaderock.backend.auth.login.model.LoginForm;
import com.shaderock.backend.auth.login.model.UserDTO;
import com.shaderock.backend.user.AppUserDetailsService;
import com.shaderock.backend.user.model.entity.AppUserDetails;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final AppUserDetailsService appUserDetailsService;
  private final JwtTokenService jwtTokenService;

  @Transactional
  public UserDTO login(@Valid final LoginForm form) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.email(), form.password()));

    AppUserDetails userDetails = appUserDetailsService.loadUserByUsername(form.email());
    return new UserDTO(userDetails.getEmail(),
                       jwtTokenService.generateToken(userDetails),
                       userDetails.getFirstName(),
                       userDetails.getLastName(),
                       userDetails.getRoles());
  }
}
