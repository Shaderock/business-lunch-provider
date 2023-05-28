package com.shaderock.lunch.backend.feature.auth;

import com.shaderock.lunch.backend.communication.mail.service.MailService;
import com.shaderock.lunch.backend.feature.auth.login.JwtTokenService;
import com.shaderock.lunch.backend.feature.auth.login.model.LoginForm;
import com.shaderock.lunch.backend.feature.auth.registration.error.exception.ConfirmationEmailNotSentException;
import com.shaderock.lunch.backend.feature.auth.registration.error.exception.TokenNotFoundException;
import com.shaderock.lunch.backend.feature.auth.registration.error.exception.UserAlreadyRegisteredException;
import com.shaderock.lunch.backend.feature.auth.registration.model.UserRegistrationForm;
import com.shaderock.lunch.backend.feature.config.preference.employee.service.EmployeePreferencesService;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.repository.AppUserDetailsRepository;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.feature.user.repository.AppUserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final AppUserDetailsService appUserDetailsService;
  private final JwtTokenService jwtTokenService;
  private final AppUserDetailsRepository appUserDetailsRepository;
  private final AppUserRepository appUserRepository;
  private final MailService mailService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final EmployeePreferencesService employeePreferencesService;

  @Transactional
  public String login(@Valid final LoginForm form) {
    LOGGER.info("Trying to sign in user with email=[{}]", form.email());
    AppUserDetails userDetails;

    try {
      userDetails = appUserDetailsService.loadUserByUsername(form.email());
      if (!passwordEncoder.matches(form.password(), userDetails.getPassword())) {
        throw new BadCredentialsException("Bad Credentials");
      }
    } catch (UsernameNotFoundException ex) {
      throw new BadCredentialsException("Bad Credentials");
    }

    LOGGER.info("User found");
    return jwtTokenService.generateToken(userDetails);
  }

  @Transactional(TxType.REQUIRES_NEW)
  public void registerUser(@Valid UserRegistrationForm form, boolean sendConfirmationEmail) {
    LOGGER.info("Attempting to register user with email=[{}]", form.email());
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
    AppUserDetails persistedDetails = appUserDetailsRepository.save(details);
    LOGGER.info("Registered user [{}]", persistedDetails);

    AppUser appUser = new AppUser();
    appUser.setUserDetails(persistedDetails);
    AppUser persistedUser = appUserRepository.save(appUser);
    employeePreferencesService.create(persistedUser);
    persistedDetails.setAppUser(persistedUser);
    appUserDetailsRepository.save(persistedDetails);
    try {
      if (sendConfirmationEmail) {
        mailService.sendConfirmationEmail(persistedDetails);
      } else {
        confirmEmail(details.getRegistrationToken());
      }
    } catch (MessagingException e) {
      throw new ConfirmationEmailNotSentException(details.getEmail());
    }
  }

  public void registerUser(@Valid UserRegistrationForm userRegistrationForm) {
    registerUser(userRegistrationForm, true);
  }

  public boolean isUserRegistered(String email) {
    return appUserDetailsRepository.findByEmail(email).isPresent();
  }

  @Transactional
  public void confirmEmail(String token) {
    AppUserDetails userDetails = appUserDetailsRepository.findByRegistrationToken(token)
        .orElseThrow(() -> new TokenNotFoundException(token));

    userDetails.setEnabled(true);
    userDetails.getRoles().add(Role.USER);
    LOGGER.info("Confirmed email for {}", userDetails);
  }
}
