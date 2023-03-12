package com.shaderock.lunch.backend.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.menu.repository.CategoryRepository;
import com.shaderock.lunch.backend.organization.model.entity.OrganizationDetails;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.repository.SupplierRepository;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.user.model.type.Role;
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
        .id(UUID.fromString("e9a4b5c5-1e9a-46b7-ae13-54cb2d7f815c"))
        .name("organization")
        .phone("+373777777")
        .email("organization@dummy.email.test")
        .build();

    supplier = Supplier.builder()
        .id(UUID.randomUUID())
        .organizationDetails(OrganizationDetails.builder()
            .id(UUID.randomUUID())
            .name("supplier")
            .build())
        .build();

    SupplierPreferences supplierPreferences = SupplierPreferences.builder()
        .supplier(supplier)
        .build();

    supplier.setPreferences(supplierPreferences);

    Menu menu = Menu.builder()
        .id(UUID.randomUUID())
        .supplier(supplier)
        .categories(new HashSet<>())
        .build();
    supplier.setMenu(menu);

    Set<Role> roles = new HashSet<>();
    roles.add(Role.USER);
    roles.add(Role.SUPPLIER);

    AppUserDetails userDetails = AppUserDetails.builder()
        .id(UUID.randomUUID())
        .email("user@dummy.email.test")
        .roles(roles)
        .build();

    AppUser appUser = AppUser.builder()
        .id(UUID.randomUUID())
        .userDetails(userDetails)
        .organizationDetails(organizationDetails)
        .build();

    userDetails.setAppUser(appUser);

    category = Category.builder()
        .id(UUID.randomUUID())
        .name("category")
        .menu(menu)
        .options(new HashSet<>())
        .build();

    categoryDto = new CategoryDto(category.getId(), category.getName(),
        category.getOptions().stream().map(Option::getId).collect(Collectors.toSet()),
        category.isOrderingAllowed(), category.getMenu().getId());
  }

  @Test
  void CreateCategory_OnUserHasNoSupplier_ReturnsBadRequest() throws Exception {
    Principal mockPrincipal = Mockito.mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("test");
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(
        anyString())).thenReturn(Optional.empty());

    mockMvc.perform(post("/api/category")
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

    mockMvc.perform(post("/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto))
            .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void CreateCategory_OnValidRequest_ReturnsBadRequest() throws Exception {
    Principal mockPrincipal = Mockito.mock(Principal.class);
    when(mockPrincipal.getName()).thenReturn("test");
    when(supplierRepository.findByOrganizationDetails_Users_UserDetails_Email(
        anyString())).thenReturn(Optional.of(supplier));
    when(categoryRepository.findByNameAndMenu_Supplier_Id(anyString(), any())).thenReturn(
        Optional.empty());
    when(categoryRepository.save(any())).thenReturn(category);

    mockMvc.perform(post("/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto))
            .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryDto.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isOrderingAllowed")
            .value(categoryDto.isOrderingAllowed()));
  }
}
