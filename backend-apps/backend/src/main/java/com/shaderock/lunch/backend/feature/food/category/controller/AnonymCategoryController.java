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
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ANONYM_CATEGORY)
@RequiredArgsConstructor
public class AnonymCategoryController {

  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;
  private final SupplierService supplierService;

  @GetMapping("/all")
  public ResponseEntity<List<CategoryDto>> read() {
    List<CategoryDto> categoryDtos = categoryService.read().stream()
        .map(categoryMapper::toDto).toList();

    return ResponseEntity.ok(categoryDtos);
  }

  @GetMapping
  @Transactional
  public ResponseEntity<List<CategoryDto>> readByName(
      @RequestParam @NotNull String supplierName) {
    Supplier supplier = supplierService.readByName(supplierName);
    List<Category> categories = categoryService.read(supplier);
    return ResponseEntity.ok(categories.stream().map(categoryMapper::toDto).toList());
  }
}
