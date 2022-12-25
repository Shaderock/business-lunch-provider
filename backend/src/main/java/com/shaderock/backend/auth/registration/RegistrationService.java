package com.shaderock.backend.auth.registration;

import com.shaderock.backend.auth.registration.error.exception.ConfirmationEmailNotSentException;
import com.shaderock.backend.auth.registration.error.exception.UserAlreadyRegisteredException;
import com.shaderock.backend.mail.MailService;
import com.shaderock.backend.auth.registration.error.exception.TokenNotFoundException;
import com.shaderock.backend.model.entity.user.AppUser;
import com.shaderock.backend.model.repository.AppUserRepository;
import com.shaderock.backend.model.type.Role;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
  private final AppUserRepository userRepository;
  private final MailService mailService;

  @Transactional
  public void registerUser(AppUser appUser) {
    if (isUserRegistered(appUser.getEmail())) {
      throw new UserAlreadyRegisteredException(appUser.getEmail());
    }

    appUser.setRegistrationToken(UUID.randomUUID().toString());

    try {
      mailService.sendConfirmationEmail(appUser);
    } catch (MessagingException e) {
      throw new ConfirmationEmailNotSentException(appUser.getEmail());
    }

    userRepository.save(appUser);
  }

  public boolean isUserRegistered(String email) {
    Optional<AppUser> userOptional = userRepository.findByEmail(email);
    return userOptional.isPresent() && userOptional.get().isEnabled();
  }

  @Transactional
  public void confirmEmail(String token) {
    Optional<AppUser> userOptional = userRepository.findByRegistrationToken(token);
    if (userOptional.isEmpty()) {
      throw new TokenNotFoundException(token);
    }

    userOptional.get().setEnabled(true);
    userOptional.get().getRoles().add(Role.USER);
  }
}
