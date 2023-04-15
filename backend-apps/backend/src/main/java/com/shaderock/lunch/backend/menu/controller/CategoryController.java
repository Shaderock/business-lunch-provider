package com.shaderock.lunch.backend.menu.controller;

import com.shaderock.lunch.backend.menu.mapper.CategoryMapper;
import com.shaderock.lunch.backend.menu.model.dto.CategoryDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.service.CategoryService;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import com.shaderock.lunch.backend.utils.ApiConstants;
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

  @GetMapping("/all")
  public ResponseEntity<List<CategoryDto>> readAllPublic() {
    List<CategoryDto> categoryDtos = categoryService.read().stream()
        .map(categoryMapper::toDto).toList();

    return ResponseEntity.ok(categoryDtos);
  }

  @GetMapping("/default")
  public ResponseEntity<List<CategoryDto>> readDefault() {
    List<CategoryDto> categoryDtos = categoryService.readAllDefault();
    return ResponseEntity.ok(categoryDtos);
  }
}
