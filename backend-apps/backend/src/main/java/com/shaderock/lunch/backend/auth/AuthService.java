package com.shaderock.lunch.backend.auth;

import com.shaderock.lunch.backend.auth.login.JwtTokenService;
import com.shaderock.lunch.backend.auth.login.model.LoginForm;
import com.shaderock.lunch.backend.auth.registration.error.exception.ConfirmationEmailNotSentException;
import com.shaderock.lunch.backend.auth.registration.error.exception.TokenNotFoundException;
import com.shaderock.lunch.backend.auth.registration.error.exception.UserAlreadyRegisteredException;
import com.shaderock.lunch.backend.auth.registration.model.UserRegistrationForm;
import com.shaderock.lunch.backend.service.mail.MailService;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import com.shaderock.lunch.backend.user.repository.UserDetailsRepository;
import com.shaderock.lunch.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final AppUserDetailsService appUserDetailsService;
  private final JwtTokenService jwtTokenService;
  private final UserDetailsRepository userDetailsRepository;
  private final UserRepository userRepository;
  private final MailService mailService;
  private final BCryptPasswordEncoder passwordEncoder;

  @Transactional
  public String login(@Valid final LoginForm form) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.email(), form.password()));
    AppUserDetails userDetails = appUserDetailsService.loadUserByUsername(form.email());
    return jwtTokenService.generateToken(userDetails);
  }

  @Transactional
  public void registerUser(@Valid UserRegistrationForm form) {
    if (isUserRegistered(form.email())) {
      throw new UserAlreadyRegisteredException(form.email());
    }

    AppUserDetails details = AppUserDetails.builder()
            .email(form.email())
            .password(passwordEncoder.encode(form.password()))
            .firstName(form.firstName())
            .lastName(form.lastName())
            .roles(new HashSet<>())
            .registrationToken(UUID.randomUUID().toString())
            .isEnabled(false)
            .build();
    AppUserDetails persistedDetails = userDetailsRepository.save(details);

    AppUser appUser = new AppUser();
    appUser.setUserDetails(persistedDetails);
    AppUser persistedUser = userRepository.save(appUser);

    persistedDetails.setAppUser(persistedUser);
    try {
      mailService.sendConfirmationEmail(persistedDetails);
    } catch (MessagingException e) {
      throw new ConfirmationEmailNotSentException(details.getEmail());
    }
  }

  public boolean isUserRegistered(String email) {
    return userDetailsRepository.findByEmail(email).isPresent();
  }

  @Transactional
  public void confirmEmail(String token) {
    AppUserDetails userDetails = userDetailsRepository.findByRegistrationToken(token)
            .orElseThrow(() -> new TokenNotFoundException(token));

    userDetails.setEnabled(true);
    userDetails.getRoles().add(Role.USER);
  }
}