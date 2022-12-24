package com.shaderock.backend.auth.registration;

import com.shaderock.backend.messaging.exception.TokenNotFoundException;
import com.shaderock.backend.model.entity.user.AppUser;
import com.shaderock.backend.model.repository.AppUserRepository;
import com.shaderock.backend.model.type.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
  private final AppUserRepository userRepository;

  @Transactional
  public void registerUser(AppUser appUser) {
    if (isUserRegistered(appUser.getEmail())) {
      throw new UserAlreadyRegisteredException(appUser.getEmail());
    }
    appUser.setRegistrationToken(UUID.randomUUID().toString());
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
