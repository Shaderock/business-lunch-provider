package com.shaderock.lunch.backend.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.type.Role;
import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.repository.CategoryRepository;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.repository.SupplierRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.util.ApiConstants;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc()
@TestPropertySource(properties = {"spring.config.name=application-integration-test",
    "spring.config.location=classpath:/"})
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CategoryControllerIntegrationTests {

  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @MockBean
  private SupplierRepository supplierRepository;
  @MockBean
  private CategoryRepository categoryRepository;

  private Supplier supplier;
  private Category category;
  private CategoryDto categoryDto;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    OrganizationDetails organizationDetails = OrganizationDetails.builder()
        .name("organization")
        .phone("+373777777")
        .email("organization@dummy.email.test")
        .build();
    organizationDetails.setId(UUID.fromString("e9a4b5c5-1e9a-46b7-ae13-54cb2d7f815c"));

    supplier = Supplier.builder()
        .organizationDetails(OrganizationDetails.builder()
            .name("supplier")
            .build())
        .build();
    supplier.setId(UUID.randomUUID());

    SupplierPreferences supplierPreferences = SupplierPreferences.builder()
        .supplier(supplier)
        .build();

    supplier.setPreferences(supplierPreferences);

    Menu menu = Menu.builder()
        .supplier(supplier)
        .categories(new HashSet<>())
        .build();
    menu.setId(UUID.randomUUID());
    supplier.setMenu(menu);

    Set<Role> roles = new HashSet<>();
    roles.add(Role.USER);
    roles.add(Role.SUPPLIER);

    AppUserDetails userDetails = AppUserDetails.builder()
        .email("user@dummy.email.test")
        .roles(roles)
        .build();
    userDetails.setId(UUID.randomUUID());

    AppUser appUser = AppUser.builder()
        .userDetails(userDetails)
        .organizationDetails(organizationDetails)
        .build();
    appUser.setId(UUID.randomUUID());

    userDetails.setAppUser(appUser);

    category = Category.builder()
        .name("category")
        .menu(menu)
        .options(new HashSet<>())
        .build();
    category.setId(UUID.randomUUID());

    categoryDto = new CategoryDto(category.getId(), category.getName(),
        category.getOptions().stream().map(Option::getId).collect(Collectors.toSet()),
        category.isPublic());
  }

  @Test
  void CreateCategory_OnUserHasNoSupplier_ReturnsBadRequest() throws Exception {
    Principal mockPrincipal = Mockito.mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("test");
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(
        anyString())).thenReturn(Optional.empty());

    mockMvc.perform(post(ApiConstants.SUPPLIER_ADM_CATEGORY)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto))
            .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void CreateCategory_OnCategoryAlreadyExists_ReturnsBadRequest() throws Exception {
    Principal mockPrincipal = Mockito.mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("test");
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(
        anyString())).thenReturn(Optional.of(supplier));
    when(categoryRepository.findByNameAndMenu_Supplier_Id(anyString(), any())).thenReturn(
        Optional.of(category));

    mockMvc.perform(post(ApiConstants.SUPPLIER_ADM_CATEGORY)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto))
            .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void CreateCategory_OnValidRequest_ReturnsCreatedDto() throws Exception {
    Principal mockPrincipal = Mockito.mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("test");
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(
        anyString())).thenReturn(Optional.of(supplier));
    when(categoryRepository.findByNameAndMenu_Supplier_Id(anyString(), any())).thenReturn(
        Optional.empty());
    when(categoryRepository.save(any())).thenReturn(category);

    mockMvc.perform(post(ApiConstants.SUPPLIER_ADM_CATEGORY)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto))
            .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryDto.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isPublic")
            .value(categoryDto.isPublic()));
  }
}
