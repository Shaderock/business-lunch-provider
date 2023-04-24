package com.shaderock.lunch.backend.feature.food.category.controller;

import com.shaderock.lunch.backend.feature.food.category.dto.CategoryDto;
import com.shaderock.lunch.backend.feature.food.category.mapper.CategoryMapper;
import com.shaderock.lunch.backend.feature.food.category.service.CategoryService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ANONYM_CATEGORY)
@RequiredArgsConstructor
public class AnonymCategoryController {

  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @GetMapping("/all")
  public ResponseEntity<List<CategoryDto>> read() {
    List<CategoryDto> categoryDtos = categoryService.read().stream()
        .map(categoryMapper::toDto).toList();

    return ResponseEntity.ok(categoryDtos);
  }

}
