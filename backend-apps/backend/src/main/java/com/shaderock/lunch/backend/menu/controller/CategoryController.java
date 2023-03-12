package com.shaderock.lunch.backend.menu.controller;

import com.shaderock.lunch.backend.menu.mapper.CategoryMapper;
import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.service.CategoryService;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
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
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final SupplierService supplierService;
  private final CategoryMapper categoryMapper;

  @PostMapping
  public ResponseEntity<CategoryDto> create(
      @RequestBody @NotNull @Valid final CategoryDto categoryDto, Principal principal) {
    Supplier supplier = supplierService.read(principal.getName());
    Category persistedCategory = categoryService.create(categoryDto, supplier);
    CategoryDto persistedCategoryDto = categoryMapper.toDto(persistedCategory);
    return ResponseEntity.ok(persistedCategoryDto);
  }

  @GetMapping
  public ResponseEntity<CategoryDto> read(@RequestParam @NotBlank final UUID id,
      Principal principal) {
    Supplier supplier = supplierService.read(principal.getName());
    Category category = categoryService.read(id, supplier);
    CategoryDto categoryDto = categoryMapper.toDto(category);
    return ResponseEntity.ok(categoryDto);
  }

  @PutMapping
  public ResponseEntity<CategoryDto> update(
      @RequestBody @NotNull @Valid final CategoryDto categoryDto, Principal principal) {
    Supplier supplier = supplierService.read(principal.getName());
    Category updatedCategory = categoryService.update(categoryDto, supplier);
    CategoryDto updatedCategoryDto = categoryMapper.toDto(updatedCategory);
    return ResponseEntity.ok(updatedCategoryDto);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam @NotNull UUID id, Principal principal) {
    Supplier supplier = supplierService.read(principal.getName());
    categoryService.delete(id, supplier);
    return ResponseEntity.noContent().build();
  }
}
