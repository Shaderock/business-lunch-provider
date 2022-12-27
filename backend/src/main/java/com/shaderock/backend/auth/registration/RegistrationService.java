package com.shaderock.backend.auth.registration;

import com.shaderock.backend.auth.registration.error.exception.ConfirmationEmailNotSentException;
import com.shaderock.backend.auth.registration.error.exception.TokenNotFoundException;
import com.shaderock.backend.auth.registration.error.exception.UserAlreadyRegisteredException;
import com.shaderock.backend.auth.registration.model.UserRegistrationForm;
import com.shaderock.backend.service.mail.MailService;
import com.shaderock.backend.user.model.entity.AppUser;
import com.shaderock.backend.user.model.entity.AppUserDetails;
import com.shaderock.backend.user.model.type.Role;
import com.shaderock.backend.user.repository.UserDetailsRepository;
import com.shaderock.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
  private final UserDetailsRepository userDetailsRepository;
  private final UserRepository userRepository;
  private final MailService mailService;
  private final BCryptPasswordEncoder passwordEncoder;

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
