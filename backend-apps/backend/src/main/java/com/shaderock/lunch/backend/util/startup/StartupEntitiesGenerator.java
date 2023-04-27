package com.shaderock.lunch.backend.util.startup;

import com.shaderock.lunch.backend.feature.auth.AuthService;
import com.shaderock.lunch.backend.feature.auth.registration.model.UserRegistrationForm;
import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.config.preference.company.entity.CompanyPreferences;
import com.shaderock.lunch.backend.feature.config.preference.company.type.CompanyDiscountType;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.CategoryTag;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.service.CategoryService;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.service.OptionService;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.organization.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
  private final CategoryService categoryService;
  private final OptionService optionService;
  private final CompanyService companyService;
  private final Faker faker;
  private final ResourceLoader resourceLoader;
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
      generateSupplier(appUserDetailsService.loadUserByUsername(Organization.SUPPLIER.adminEmail));
      List<Supplier> suppliers = generateDummySuppliers();
      suppliers.forEach(this::generateDefaultCategories);
    } catch (Exception e) {
      LOGGER.error("Couldn't generate default supplier. Reason: {}", e.getMessage());
    }
    try {
      generateCompany(appUserDetailsService.loadUserByUsername(Organization.COMPANY.adminEmail));
      generateDummyEmployees();
    } catch (Exception e) {
      LOGGER.error("Couldn't generate default company. Reason: {}", e.getMessage());
    }
  }

  private List<Supplier> generateDummySuppliers() {
    List<Supplier> suppliers = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      Optional<AppUserDetails> generatedUser = generateUser(
          faker.internet().safeEmailAddress(),
          "test",
          faker.name().firstName(),
          faker.name().lastName(),
          Set.of(Role.USER));

      if (generatedUser.isPresent()) {
        Supplier supplier = generateSupplier(generatedUser.get());
        suppliers.add(supplier);
      }
    }
    return suppliers;
  }

  @SneakyThrows
  private Supplier generateSupplier(AppUserDetails userDetails) {
    supplierService.register(
        new OrganizationRegistrationForm(
            faker.restaurant().name() + faker.number().numberBetween(1, 100)),
        userDetails);
    Supplier supplier = supplierService.read(userDetails.getEmail());
    supplier.setPublic(true);

    String websiteUrl = faker.internet().url();
    supplier.setWebsiteUrl(new URI(websiteUrl));
    supplier.setMenuUrl(new URI(websiteUrl + "/menu"));

    OrganizationDetails organizationDetails = supplier.getOrganizationDetails();
    organizationDetails.setEmail(faker.internet().safeEmailAddress());
    organizationDetails.setPhone(faker.phoneNumber().phoneNumberInternational());
    organizationDetails.setDescription(faker.restaurant().description());

    organizationDetails.setLogo(getImageBytes(String.valueOf(faker.number().numberBetween(1, 40))));

    SupplierPreferences preferences = supplier.getPreferences();
    preferences.setWorkDayStart(
        LocalTime.of(faker.number().numberBetween(8, 11), faker.number().numberBetween(0, 59)));

    preferences.setWorkDayEnd(
        LocalTime.of(faker.number().numberBetween(15, 21), faker.number().numberBetween(0, 59)));

    preferences.setMinimumOrdersPerCompanyRequest(faker.number().numberBetween(1, 20));
    preferences.setMinimumCategoriesForEmployeeOrder(faker.number().numberBetween(1, 3));

    preferences.setRequestOffset(Duration.ofDays(faker.number().numberBetween(0, 1))
        .plusHours(faker.number().numberBetween(0, 3))
        .plusMinutes(faker.number().numberBetween(15, 45)));

    List<OrderType> orderTypes = Arrays.asList(OrderType.values());
    preferences.setOrderType(orderTypes.get(faker.number().numberBetween(0, orderTypes.size())));

    List<CategoryTag> categoryTagList = new ArrayList<>(Arrays.asList(CategoryTag.values()));
    int categoriesNumber = faker.number().numberBetween(1, 7);
    List<CategoryTag> supplierCategoriesTags = new ArrayList<>();
    for (int i = 0; i < categoriesNumber; i++) {
      int index = faker.number().numberBetween(0, categoryTagList.size());
      CategoryTag tag = categoryTagList.remove(index);
      supplierCategoriesTags.add(tag);
    }
    preferences.setCategoriesTags(supplierCategoriesTags);
    return supplier;
  }

  private void generateDefaultCategories(Supplier supplier) {

    if (supplier.getPreferences().getOrderType() == OrderType.UNLIMITED_OPTIONS) {
      int publicCategoriesAmount = faker.number().numberBetween(3, 6);
      int privateCategoriesAmount = faker.number().numberBetween(0, 1);

      generateCategories(supplier, publicCategoriesAmount, true);
      generateCategories(supplier, privateCategoriesAmount, false);
    }
  }

  private List<Category> generateCategories(Supplier supplier, int amount, boolean isPublic) {
    List<Category> categories = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      Category category = new Category();
      category.setName(generateCategoryName());

      Category persistedCategory = categoryService.create(category, supplier);
      persistedCategory.setPublic(isPublic);
      persistedCategory = categoryService.update(persistedCategory, supplier);

      int publicOptionsPerCategory = isPublic ? 15 : 0;
      int privateOptionsPerCategory = isPublic ? 3 : 15;
      generateOptions(supplier, persistedCategory, publicOptionsPerCategory, true);
      generateOptions(supplier, persistedCategory, privateOptionsPerCategory, false);
    }
    return categories;
  }

  private String generateCategoryName() {
    List<String> categoryNames = Arrays.asList(faker.food().dish(), faker.food().fruit(),
        faker.food().sushi(), faker.food().vegetable(), faker.dessert().variety());
    return categoryNames.get(faker.number().numberBetween(0, categoryNames.size()));
  }

  private void generateOptions(Supplier supplier, Category category, int amount, boolean isPublic) {
    for (int i = 0; i < amount; i++) {
      Option option = new Option();
      option.setName(faker.food().ingredient());
      option.setDescription(faker.lorem().paragraph());
      option.setPrice(faker.number().randomDouble(2, 1, 100));
      option.setGram(generateGram());

      Option persistedOption = optionService.create(option, category);
      persistedOption.setPublic(isPublic);
      optionService.update(persistedOption, supplier, category);

      int randomNumber = faker.number().numberBetween(0, 100);
      if (randomNumber < 70) {
        persistedOption.setPhoto(
            getImageBytes(String.valueOf(faker.number().numberBetween(1, 40))));
        optionService.update(persistedOption, supplier, category);
      }
    }
  }

  private String generateGram() {
    int numOptions = faker.number().numberBetween(1, 4);
    List<String> options = new ArrayList<>();
    for (int i = 0; i < numOptions; i++) {
      int weight = faker.number().numberBetween(15, 100);
      options.add(weight + "g");
    }
    return String.join("/", options);
  }

  private void generateCompany(AppUserDetails userDetails) {
    companyService.register(new OrganizationRegistrationForm(faker.company().name()), userDetails);

    Company company = companyService.read(userDetails.getEmail());

    OrganizationDetails organizationDetails = company.getOrganizationDetails();
    organizationDetails.setEmail(faker.internet().safeEmailAddress());
    organizationDetails.setPhone(faker.phoneNumber().phoneNumberInternational());
    organizationDetails.setDescription(faker.company().catchPhrase());

    CompanyPreferences preferences = company.getPreferences();
    preferences.setDeliveryAddress(faker.address().fullAddress());
    preferences.setDiscountPerDay(faker.number().randomDouble(2, 0, 50));
    preferences.setDiscountFixFirstOrder(faker.number().randomDouble(2, 0, 40));
    preferences.setMaxDiscountFixFirstOrder(faker.number().randomDouble(2, 0, 40));
    preferences.setDiscountPercentageFirstOrder(faker.number().numberBetween(15, 50));
    preferences.setDeliverAt(LocalTime.of(faker.number().numberBetween(11, 19),
        faker.number().numberBetween(0, 59)));

    List<CompanyDiscountType> companyDiscountTypes = Arrays.asList(CompanyDiscountType.values());

    preferences.setCompanyDiscountType(
        companyDiscountTypes.get(faker.number().numberBetween(0, companyDiscountTypes.size())));
  }

  private void generateDummyEmployees() {
    Company company = companyService.read(Organization.COMPANY.adminEmail);
    for (int i = 0; i < 20; i++) {
      Optional<AppUserDetails> generatedUser = generateUser(
          faker.internet().safeEmailAddress(),
          "test",
          faker.name().firstName(),
          faker.name().lastName(),
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

  public byte[] getImageBytes(String fileName) {
    try {
      Resource resource = resourceLoader.getResource(
          "classpath:generation/" + fileName + ".jpg");
      InputStream inputStream = resource.getInputStream();
      return inputStream.readAllBytes();
    } catch (Exception e) {
      return null;
    }
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
