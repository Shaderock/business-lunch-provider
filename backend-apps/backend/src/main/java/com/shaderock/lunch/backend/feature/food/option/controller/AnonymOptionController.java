package com.shaderock.lunch.backend.feature.food.option.controller;

import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.category.service.CategoryService;
import com.shaderock.lunch.backend.feature.food.option.dto.OptionDto;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.food.option.mapper.OptionMapper;
import com.shaderock.lunch.backend.feature.food.option.service.OptionService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import com.shaderock.lunch.backend.util.ImageService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ANONYM_OPTION)
@RequiredArgsConstructor
public class AnonymOptionController {

  private final OptionService optionService;
  private final SupplierService supplierService;
  private final CategoryService categoryService;
  private final OptionMapper optionMapper;
  private final ImageService imageService;

  @GetMapping("/by-option-id")
  public ResponseEntity<OptionDto> read(@RequestParam @NotBlank final UUID optionId) {
    Option option = optionService.read(optionId);
    return ResponseEntity.ok(optionMapper.toDto(option));
  }

  @GetMapping("/by-supplier-id-and-category-id")
  public ResponseEntity<List<OptionDto>> readAllBySupplierIdAndCategoryId(
      @RequestParam @NotBlank final UUID supplierId,
      @RequestParam @NotBlank final UUID categoryId) {

    Supplier supplier = supplierService.read(supplierId);
    Category category = categoryService.read(categoryId);
    List<Option> options = optionService.read(supplier, category);
    return ResponseEntity.ok(options.stream().map(optionMapper::toDto).toList());
  }

  @GetMapping("/by-supplier-id")
  public ResponseEntity<List<OptionDto>> readAllBySupplierId(
      @RequestParam @NotBlank final UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);
    List<Option> options = optionService.read(supplier);
    return ResponseEntity.ok(options.stream().map(optionMapper::toDto).toList());
  }

  @GetMapping("/by-category-id")
  @Transactional
  public ResponseEntity<List<OptionDto>> readAllByCategoryId(
      @RequestParam @NotBlank final UUID categoryId) {
    Category category = categoryService.read(categoryId);
    List<Option> options = optionService.read(category);

    return ResponseEntity.ok(options.stream().map(optionMapper::toDto).toList());
  }

  @SneakyThrows
  @GetMapping(value = "/photo", produces = MediaType.IMAGE_JPEG_VALUE)
  @Transactional
  @Cacheable(value = "supplierOptionPhoto", key = "#optionId")
  public byte[] readPhoto(@RequestParam @NotNull UUID optionId) {
    Option option = optionService.read(optionId);
    return option.getPhoto();
  }

  @SneakyThrows
  @GetMapping(value = "/photo/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
  @Transactional
  @Cacheable(value = "supplierOptionThumbnail", key = "#optionId")
  public byte[] readThumbnail(@RequestParam @NotNull UUID optionId) {
    Option option = optionService.read(optionId);

    byte[] logo = option.getPhoto();
    if (logo != null) {
      logo = imageService.resizeToThumbnail(logo);
    }
    return logo;
  }
}
