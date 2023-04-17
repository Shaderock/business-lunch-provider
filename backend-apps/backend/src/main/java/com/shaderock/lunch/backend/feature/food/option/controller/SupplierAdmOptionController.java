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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping(ApiConstants.SUPPLIER_ADM_OPTION)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SupplierAdmOptionController {

  private final OptionService optionService;
  private final CategoryService categoryService;
  private final SupplierService supplierService;
  private final OptionMapper optionMapper;

  @PostMapping
  public ResponseEntity<OptionDto> create(
      @RequestBody @NotNull @Valid final OptionDto optionDto,
      @RequestParam @NotBlank final UUID categoryId,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Category category = categoryService.read(categoryId, supplier);
    Option option = optionService.create(optionDto, category);
    return ResponseEntity.ok(optionMapper.toDto(option));
  }

  @GetMapping
  public ResponseEntity<OptionDto> read(@RequestParam @NotBlank final UUID id,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Option option = optionService.read(id, supplier);
    return ResponseEntity.ok(optionMapper.toDto(option));
  }

  @GetMapping("/all")
  public ResponseEntity<List<OptionDto>> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    List<Option> options = optionService.read(supplier);
    return ResponseEntity.ok(options.stream().map(optionMapper::toDto).toList());
  }

  @PutMapping
  public ResponseEntity<OptionDto> update(
      @RequestBody @NotNull @Valid final OptionDto optionDto,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    Option option = optionService.update(optionDto, supplier);
    return ResponseEntity.ok(optionMapper.toDto(option));
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@RequestParam @NotNull UUID id, Principal principal) {
    Supplier supplier = supplierService.read(principal);
    optionService.delete(id, supplier);
    return ResponseEntity.noContent().build();
  }

}