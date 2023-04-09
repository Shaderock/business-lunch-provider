package com.shaderock.lunch.backend.user;

import com.shaderock.lunch.backend.auth.AuthService;
import com.shaderock.lunch.backend.auth.registration.model.UserRegistrationForm;
import com.shaderock.lunch.backend.organization.company.service.CompanyService;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
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
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupTestDataGenerator implements
    ApplicationListener<ContextRefreshedEvent> {

  public static final String DEFAULT_USER_PASSWORD = "test";
  private final AuthService authService;
  private final AppUserService appUserService;
  private final AppUserDetailsService appUserDetailsService;
  private final SupplierService supplierService;
  private final CompanyService companyService;
  @Value(value = "${lunch.backend.system.admin.email}")
  private String sysAdminEmail;
  @Value(value = "${lunch.backend.system.admin.password}")
  private String sysAdminPassword;
  @Value(value = "${lunch.backend.users.generate.default}")
  private boolean shouldDefaultUsersBeGenerated;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (isRegistrationAllowedForEmail(sysAdminEmail)) {
      generateSystemAdmin();
    }
    if (shouldDefaultUsersBeGenerated) {
      generateDefaultUsers();
      generateDefaultOrganizations();
    }
  }

  private void generateDefaultOrganizations() {
    supplierService.register(new OrganizationRegistrationForm(Organization.SUPPLIER.name),
        appUserDetailsService.loadUserByUsername(User.DEFAULT_SUPPLIER_EMAIL.email));
    companyService.register(new OrganizationRegistrationForm(Organization.COMPANY.name),
        appUserDetailsService.loadUserByUsername(User.DEFAULT_COMPANY_ADMIN_EMAIL.email));
  }

  private void generateDefaultUsers() {
    LOGGER.info("Generating default users");
    for (User user : User.values()) {
      if (isRegistrationAllowedForEmail(user.email)) {
        generateUser(user.email, user.roles);
      }
    }
  }

  private void generateUser(String email, Set<Role> roles) {
    LOGGER.info("Generating user(email={}, roles={}. Check vault for credentials.", email, roles);
    UserRegistrationForm registrationForm = new UserRegistrationForm(email,
        DEFAULT_USER_PASSWORD, email, email);
    authService.registerUser(registrationForm, false);

    LOGGER.info("User generated");
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

  private boolean isRegistrationAllowedForEmail(String email) {
    boolean userRegistered = authService.isUserRegistered(email);

    if (userRegistered) {
      LOGGER.info("User(email=[{}]) already exists", email);
    }

    return !userRegistered;
  }

  @RequiredArgsConstructor
  private enum User {
    DEFAULT_APP_USER_EMAIL("user@dummy", Set.of(Role.USER)),
    DEFAULT_EMPLOYEE_EMAIL("employee@dummy", Set.of(Role.USER)),
    DEFAULT_COMPANY_ADMIN_EMAIL("admin@dummy", Set.of(Role.USER)),
    DEFAULT_SUPPLIER_EMAIL("supplier@dummy", Set.of(Role.USER));

    private final String email;
    private final Set<Role> roles;
  }

  @RequiredArgsConstructor
  private enum Organization {
    COMPANY("company-organization"),
    SUPPLIER("supplier-organization");
    private final String name;
  }
}
