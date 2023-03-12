package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.auth.AuthService;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupUserGenerator implements
    ApplicationListener<ContextRefreshedEvent> {

  private static final String DEFAULT_USER_EMAIL = "user@test.test.test";
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AuthService authService;
  private final AppUserService appUserService;
  @Value(value = "${lunch.backend.system.admin.email}")
  private String sysAdminEmail;
  @Value(value = "${lunch.backend.system.admin.password}")
  private String sysAdminPassword;
  @Value(value = "${lunch.backend.users.generate.default}")
  private boolean shouldDefaultUsersBeGenerated;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (!isUserAlreadyRegistered(sysAdminEmail)) {
      generateSystemAdmin();
    }
    if (shouldDefaultUsersBeGenerated) {
      generateDefaultUsers();
    }
  }

  private void generateDefaultUsers() {
    LOGGER.info("Generating default users");
    if (!isUserAlreadyRegistered(DEFAULT_USER_EMAIL)) {
      generateAppUser();
    }
  }

  private void generateAppUser() {
    LOGGER.info("Generating default AppUser. Check vault for credentials.");
    try {
      AppUserDetails appUserDetails = AppUserDetails.builder()
          .email(DEFAULT_USER_EMAIL)
          .password(bCryptPasswordEncoder.encode("test"))
          .roles(Set.of(Role.USER))
          .isEnabled(true)
          .firstName("user")
          .lastName("user")
          .build();

      AppUser appUser = AppUser.builder()
          .userDetails(appUserDetails)
          .build();

      appUserService.create(appUser);
      LOGGER.info("AppUser generated");
    } catch (ValidationException e) {
      LOGGER.error("Couldn't generate AppUser. Reason: {}", e.getMessage());
    }
  }

  private void generateSystemAdmin() {
    LOGGER.info("Generating default system admin. Check vault for credentials.");
    try {
      AppUserDetails appUserDetails = AppUserDetails.builder()
          .email(sysAdminEmail)
          .password(sysAdminPassword)
          .roles(Set.of(Role.SYSTEM_ADMIN, Role.USER))
          .isEnabled(true)
          .firstName("sysadmin")
          .lastName("sysadmin")
          .build();

      AppUser appUser = AppUser.builder()
          .userDetails(appUserDetails)
          .build();

      appUserService.create(appUser);
      LOGGER.info("System admin generated");
    } catch (ValidationException e) {
      LOGGER.error("Couldn't generate system admin. Reason: {}", e.getMessage());
    }
  }

  private boolean isUserAlreadyRegistered(String email) {
    boolean userRegistered = authService.isUserRegistered(sysAdminEmail);

    if (userRegistered) {
      LOGGER.info("User(email=[{}]) already exists", email);
    }

    return userRegistered;
  }
}
