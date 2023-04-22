package com.shaderock.lunch.backend.util.startup;

import com.shaderock.lunch.backend.feature.auth.AuthService;
import com.shaderock.lunch.backend.feature.auth.registration.model.UserRegistrationForm;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
public class StartupEntitiesGenerator implements
    ApplicationListener<ContextRefreshedEvent> {

  public static final String DEFAULT_USER_PASSWORD = "test";
  private final AuthService authService;
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
  public void onApplicationEvent(
      @SuppressWarnings("NullableProblems") ContextRefreshedEvent event) {
    generateSystemAdmin();

    if (shouldDefaultUsersBeGenerated) {
      generateDefaultUsers();
      generateDefaultOrganizations();
    }
  }

  private void generateDefaultOrganizations() {
    try {
      generateDefaultSupplier();
    } catch (Exception e) {
      LOGGER.error("Couldn't generate default supplier. Reason: {}", e.getMessage());
    }

    try {
      generateDefaultCompany();
      generateDefaultEmployees();
    } catch (Exception e) {
      LOGGER.error("Couldn't generate default company. Reason: {}", e.getMessage());
    }
  }

  private void generateDefaultSupplier() {
    supplierService.register(new OrganizationRegistrationForm(Organization.SUPPLIER.name),
        appUserDetailsService.loadUserByUsername(Organization.SUPPLIER.adminEmail));
    Supplier supplier = supplierService.read(Organization.SUPPLIER.adminEmail);
    supplier.setPublic(true);

    OrganizationDetails organizationDetails = supplier.getOrganizationDetails();
    organizationDetails.setEmail(Organization.SUPPLIER.adminEmail);
    organizationDetails.setPhone("+37377777777");
    organizationDetails.setDescription("A dummy supplier");

    SupplierPreferences preferences = supplier.getPreferences();
    preferences.setDeliveryPeriodStartTime(LocalTime.of(10, 0));
    preferences.setDeliveryPeriodEndTime(LocalTime.of(18, 0));
    preferences.setMinimumOrdersPerCompanyRequest(2);
    preferences.setOrderType(OrderType.UNLIMITED_OPTIONS);
    preferences.setRequestOffset(Duration.of(2, ChronoUnit.HOURS));
  }

  private void generateDefaultCompany() {
    companyService.register(new OrganizationRegistrationForm(Organization.COMPANY.name),
        appUserDetailsService.loadUserByUsername(Organization.COMPANY.adminEmail));

    Company company = companyService.read(Organization.COMPANY.adminEmail);

    OrganizationDetails organizationDetails = company.getOrganizationDetails();
    organizationDetails.setEmail(Organization.COMPANY.adminEmail);
    organizationDetails.setPhone("+37388888888");
    organizationDetails.setDescription("A dummy company");

    CompanyPreferences preferences = company.getPreferences();
    preferences.setDeliveryAddress("A dummy delivery address");
    preferences.setCompanyDiscountType(CompanyDiscountType.SPECIFIC_PER_DAY);
    preferences.setDiscountPerDay(55.55);
    preferences.setDiscountFixFirstOrder(44.44);
    preferences.setMaxDiscountFixFirstOrder(33.33);
    preferences.setDiscountPercentageFirstOrder(50);
    preferences.setDeliverAt(LocalTime.of(13, 0));
  }

  private void generateDefaultEmployees() {
    Company company = companyService.read(Organization.COMPANY.adminEmail);
    for (int i = 0; i < 100; i++) {
      Optional<AppUserDetails> generatedUser = generateUser(
          String.format("employee%s@dummy.test.mail", i),
          "test",
          String.format("First Name [%s]", i),
          String.format("Last Name [%s]", i),
          Set.of(Role.EMPLOYEE));

      generatedUser.ifPresent(userDetails -> {
        userDetails.getAppUser().setOrganizationDetails(company.getOrganizationDetails());
        company.getOrganizationDetails().getUsers().add(userDetails.getAppUser());
      });
    }
  }

  private void generateDefaultUsers() {
    LOGGER.info("Generating default users");
    for (User user : User.values()) {
      generateUser(user.email, DEFAULT_USER_PASSWORD, user.firstName, user.lastName, user.roles);
    }
  }

  private Optional<AppUserDetails> generateUser(String email, String password, String firstName,
      String lastName,
      Set<Role> roles) {
    UserRegistrationForm registrationForm = new UserRegistrationForm(email, password, firstName,
        lastName);
    AppUserDetails userDetails;
    LOGGER.info("Generating [{}]", registrationForm);

    try {
      authService.registerUser(registrationForm, false);

      userDetails = appUserDetailsService.loadUserByUsername(
          registrationForm.email());
      roles.forEach(role -> userDetails.getRoles().add(role));
    } catch (Exception e) {
      LOGGER.error("Couldn't generate user. Reason: {}", e.getMessage());
      return Optional.empty();
    }

    LOGGER.info("User generated");
    return Optional.of(userDetails);
  }

  private void generateSystemAdmin() {
    LOGGER.info("Generating default system admin. Check vault for credentials.");
    generateUser(sysAdminEmail, sysAdminPassword, "sysadmin", "sysadmin",
        Set.of(Role.SYSTEM_ADMIN, Role.USER));
  }

  @RequiredArgsConstructor
  private enum User {
    DEFAULT_APP_USER_EMAIL("user@dummy", "user", "user", Set.of(Role.USER)),
    DEFAULT_EMPLOYEE_EMAIL("employee@dummy", "employee", "employee", Set.of(Role.USER)),
    DEFAULT_COMPANY_ADMIN_EMAIL("admin@dummy", "admin", "admin", Set.of(Role.USER)),
    DEFAULT_SUPPLIER_EMAIL("supplier@dummy", "supplier", "supplier", Set.of(Role.USER));

    private final String email;
    private final String firstName;
    private final String lastName;
    private final Set<Role> roles;
  }

  @RequiredArgsConstructor
  private enum Organization {
    COMPANY("company-organization", User.DEFAULT_COMPANY_ADMIN_EMAIL.email),
    SUPPLIER("supplier-organization", User.DEFAULT_SUPPLIER_EMAIL.email);
    private final String name;
    private final String adminEmail;
  }
}
