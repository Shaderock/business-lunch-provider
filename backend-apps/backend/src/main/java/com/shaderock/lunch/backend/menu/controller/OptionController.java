package com.shaderock.lunch.backend.menu.controller;

import com.shaderock.lunch.backend.menu.mapper.OptionMapper;
import com.shaderock.lunch.backend.menu.model.dto.OptionDto;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.menu.service.CategoryService;
import com.shaderock.lunch.backend.menu.service.OptionService;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import com.shaderock.lunch.backend.utils.ApiConstants;
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
@RequestMapping(ApiConstants.OPTION)
@RequiredArgsConstructor
public class OptionController {

  private final OptionService optionService;
  private final SupplierService supplierService;
  private final CategoryService categoryService;
  private final OptionMapper optionMapper;

  @GetMapping("/by-option-id")
  public ResponseEntity<OptionDto> read(@RequestParam @NotBlank final UUID id) {
    Option option = optionService.read(id);
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
}
