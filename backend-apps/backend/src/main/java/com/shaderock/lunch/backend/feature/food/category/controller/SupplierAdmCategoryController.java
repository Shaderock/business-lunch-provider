package com.shaderock.lunch.backend.feature.food.category.controller;

import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.mapper.CategoryMapper;
import com.shaderock.lunch.backend.feature.food.category.service.CategoryService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.SUPPLIER_ADM_CATEGORY)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SupplierAdmCategoryController {

  private final CategoryService categoryService;
  private final SupplierService supplierService;
  private final CategoryMapper categoryMapper;

  @PostMapping
  public ResponseEntity<CategoryDto> create(
      @RequestBody @NotNull @Valid final CategoryDto categoryDto, Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Category persistedCategory = categoryService.create(categoryDto, supplier);
    CategoryDto persistedCategoryDto = categoryMapper.toDto(persistedCategory);
    return ResponseEntity.ok(persistedCategoryDto);
  }

  @GetMapping
  @Transactional
  public ResponseEntity<CategoryDto> read(@RequestParam @NotBlank final UUID categoryId,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Category category = categoryService.read(categoryId, supplier);
    CategoryDto categoryDto = categoryMapper.toDto(category);
    return ResponseEntity.ok(categoryDto);
  }

  @GetMapping("/all")
  @Transactional
  public ResponseEntity<List<CategoryDto>> readAll(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<Category> categories = categoryService.read(supplier);
    return ResponseEntity.ok(categories.stream().map(categoryMapper::toDto).toList());
  }

  @PutMapping
  @Transactional
  public ResponseEntity<CategoryDto> update(
      @RequestBody @NotNull @Valid final CategoryDto categoryDto, Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Category updatedCategory = categoryService.update(categoryDto, supplier);
    CategoryDto updatedCategoryDto = categoryMapper.toDto(updatedCategory);
    return ResponseEntity.ok(updatedCategoryDto);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam @NotNull UUID categoryId, Principal principal) {
    Supplier supplier = supplierService.read(principal);
    categoryService.delete(categoryId, supplier);
    return ResponseEntity.noContent().build();
  }
}
