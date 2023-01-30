package com.shaderock.lunch.backend.order;

import com.shaderock.lunch.backend.order.model.dto.CategoryDto;
import com.shaderock.lunch.backend.order.service.CategoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Transactional
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<CategoryDto> create(
      @RequestBody @NotNull @Valid final CategoryDto categoryDto) {
    CategoryDto persistedCategoryDto = categoryService.createAndMapToDto(categoryDto);
    return ResponseEntity.ok(persistedCategoryDto);
  }

  @GetMapping
  public ResponseEntity<CategoryDto> read(@RequestParam @NotBlank final String name) {
    return ResponseEntity.ok(categoryService.readAndMapToDto(name));
  }

  @PutMapping
  public ResponseEntity<CategoryDto> update(
      @RequestBody @NotNull @Valid final CategoryDto categoryDto) {
    CategoryDto updatedCategoryDto = categoryService.updateAndMapToDto(categoryDto);
    return ResponseEntity.ok(updatedCategoryDto);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam @NotNull long id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
