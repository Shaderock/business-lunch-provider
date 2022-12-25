package com.shaderock.backend.auth.login;

import com.shaderock.backend.auth.login.model.LoginForm;
import com.shaderock.backend.auth.login.model.UserDTO;
import com.shaderock.backend.model.entity.user.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenService jwtTokenService;

  @PostMapping
  public ResponseEntity<UserDTO> login(@RequestBody @Valid final LoginForm loginForm) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(),
                                                                                 loginForm.getPassword()));
    } catch (final BadCredentialsException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    AppUser appUser = (AppUser) jwtUserDetailsService.loadUserByUsername(loginForm.getEmail());
    UserDTO response = UserDTO.builder()
            .email(appUser.getEmail())
            .firstName(appUser.getFirstName())
            .lastName(appUser.getLastName())
            .token(jwtTokenService.generateToken(appUser))
            .roles(appUser.getRoles())
            .build();

    return ResponseEntity.ok(response);
  }
}
