package com.shaderock.lunch.backend.feature.food.price.controller;

import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPrice;
import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPriceDto;
import com.shaderock.lunch.backend.feature.food.price.mapper.CategoriesPriceMapper;
import com.shaderock.lunch.backend.feature.food.price.repository.CategoriesPriceRepository;
import com.shaderock.lunch.backend.feature.food.price.service.CategoriesPriceService;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SUPPLIER_ADM_CATEGORIES_PRICE)
public class SupplierAdmCategoriesPricesController {

  private final SupplierService supplierService;
  private final CategoriesPriceMapper categoriesPriceMapper;
  private final CategoriesPriceService categoriesPriceService;
  private final CategoriesPriceRepository categoriesPriceRepository;

  @GetMapping("/all")
  public ResponseEntity<List<CategoriesPriceDto>> readAll(Principal principal) {
    Supplier supplier = supplierService.read(principal);

    return ResponseEntity.ok(
        supplier.getPreferences().getCategoriesPrices().stream()
            .map(categoriesPriceMapper::toDto)
            .toList());
  }

  @PostMapping
  public ResponseEntity<CategoriesPriceDto> create(
      @RequestBody @NotNull @Valid CategoriesPriceDto categoriesPriceDto, Principal principal) {
    Supplier supplier = supplierService.read(principal);
    CategoriesPrice categoriesPrice = categoriesPriceService.create(categoriesPriceDto, supplier);

    return ResponseEntity.ok(categoriesPriceMapper.toDto(categoriesPrice));
  }

  @PutMapping
  public ResponseEntity<CategoriesPriceDto> update(
      @RequestBody @NotNull @Valid CategoriesPriceDto categoriesPriceDto, Principal principal) {
    Supplier supplier = supplierService.read(principal);
    CategoriesPrice categoriesPrice = categoriesPriceService.update(categoriesPriceDto, supplier);

    return ResponseEntity.ok(categoriesPriceMapper.toDto(categoriesPrice));
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    supplier.getPreferences().getCategoriesPrices().stream()
        .max(Comparator.comparingInt(CategoriesPrice::getAmount))
        .ifPresent(categoriesPriceRepository::delete);

    return ResponseEntity.noContent().build();
  }
}
