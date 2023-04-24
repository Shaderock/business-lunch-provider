package com.shaderock.lunch.backend.feature.food.category.controller;

import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.mapper.CategoryMapper;
import com.shaderock.lunch.backend.feature.food.category.service.CategoryService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.CATEGORY)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final SupplierService supplierService;
  private final CategoryMapper categoryMapper;

  @GetMapping("/by-category-id")
  public ResponseEntity<CategoryDto> readPublic(@RequestParam @NotBlank final UUID id) {
    Category category = categoryService.read(id);
    CategoryDto categoryDto = categoryMapper.toDto(category);
    return ResponseEntity.ok(categoryDto);
  }

  @GetMapping("/by-supplier-id")
  public ResponseEntity<List<CategoryDto>> readPublicBySupplierId(
      @RequestParam @NotBlank final UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);
    List<Category> categories = categoryService.read(supplier);
    return ResponseEntity.ok(categories.stream().map(categoryMapper::toDto).toList());
  }
}
