package com.shaderock.lunch.backend.menu.service;

import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.mapper.CategoryMapper;
import com.shaderock.lunch.backend.feature.food.category.repository.CategoryRepository;
import com.shaderock.lunch.backend.feature.food.category.service.CategoryService;
import com.shaderock.lunch.backend.feature.food.menu.entity.Menu;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.organization.entity.OrganizationDetails;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTests {

  @Mock
  private CategoryRepository categoryRepository;
  @Mock
  private CategoryMapper categoryMapper;
  @InjectMocks
  private CategoryService categoryService;
  private Category category;
  private Supplier supplier;
  private CategoryDto categoryDto;

  @BeforeEach
  public void init() {
    supplier = Supplier.builder()
        .organizationDetails(OrganizationDetails.builder()
            .name("supplier")
            .build())
        .build();
    supplier.setId(UUID.randomUUID());

    Menu menu = Menu.builder()
        .supplier(supplier)
        .categories(new HashSet<>())
        .build();
    menu.setId(UUID.randomUUID());
    supplier.setMenu(menu);

    category = Category.builder()
        .name("category")
        .menu(menu)
        .options(new HashSet<>())
        .build();
    category.setId(UUID.randomUUID());

    categoryDto =
        new CategoryDto(
            category.getId(),
            category.getName(),
            category.getOptions().stream()
                .map(Option::getId)
                .collect(Collectors.toSet()),
            category.isPublic());
  }

  @Test
  void CreateCategory_OnValidDto_ReturnsCreatedCategory() {
    when(categoryMapper.toEntity(any())).thenReturn(category);
    when(categoryRepository.findByNameAndMenu_Supplier_Id(anyString(), any())).thenReturn(
        Optional.empty());
    when(categoryRepository.save(any())).thenReturn(category);

    category.setMenu(null);

    Category created = categoryService.create(categoryDto, supplier);

    assertNotNull(created);
    assertNotNull(created.getMenu());
    assertThatCollection(created.getMenu().getCategories()).contains(category);
  }

  @Test
  void CreateCategory_OnCategoryAlreadyExists_CrudValidationExceptionThrown() {
    when(categoryRepository.findByNameAndMenu_Supplier_Id(anyString(), any())).thenReturn(
        Optional.of(category));
    assertThrows(CrudValidationException.class, () -> categoryService.create(category, supplier));
  }

  @Test
  void ReadCategory_OnCategoryWithProvidedIdNotExists_CrudValidationExceptionThrown() {
    when(categoryRepository.findById(any())).thenReturn(Optional.empty());
    UUID categoryId = category.getId();
    assertThrows(CrudValidationException.class, () -> categoryService.read(categoryId));
  }

  @Test
  void ReadCategory_OnCategoryWithProvidedIdAndSupplierNotExists_CrudValidationExceptionThrown() {
    when(categoryRepository.findByIdAndMenu_Supplier_Id(any(), any())).thenReturn(Optional.empty());
    UUID categoryId = category.getId();
    assertThrows(CrudValidationException.class, () -> categoryService.read(categoryId, supplier));
  }

  @Test
  void ReadCategory_OnValidProvidedId_ReturnsCategory() {
    when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

    Category readCategory = categoryService.read(category.getId());
    assertNotNull(readCategory);
  }

  @Test
  void ReadCategory_OnValidProvidedIdAndSupplier_ReturnsCategory() {
    when(categoryRepository.findByIdAndMenu_Supplier_Id(any(), any())).thenReturn(
        Optional.of(category));

    Category readCategory = categoryService.read(category.getId(), supplier);
    assertNotNull(readCategory);
  }

  @Test
  void UpdateCategory_OnValidCategoryDtoProvided_ReturnsUpdatedCategory() {
    when(categoryMapper.toEntity(any())).thenReturn(category);
    when(categoryRepository.save(any())).thenReturn(category);
    when(categoryRepository.findByIdAndMenu_Supplier_Id(any(), any())).thenReturn(
        Optional.of(category));

    Category updated = categoryService.update(categoryDto, supplier);
    assertNotNull(updated);
    assertEquals(categoryDto.id(), updated.getId());
    assertEquals(categoryDto.name(), updated.getName());
    assertEquals(categoryDto.isPublic(), updated.isPublic());
    assertEquals(categoryDto.optionIds().size(), updated.getOptions().size());
  }
}
