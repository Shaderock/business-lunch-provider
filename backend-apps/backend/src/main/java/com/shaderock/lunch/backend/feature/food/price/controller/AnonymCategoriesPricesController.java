package com.shaderock.lunch.backend.feature.food.price.controller;

import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPriceDto;
import com.shaderock.lunch.backend.feature.food.price.mapper.CategoriesPriceMapper;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ANONYM_CATEGORIES_PRICE)
public class AnonymCategoriesPricesController {

  private final SupplierService supplierService;
  private final CategoriesPriceMapper categoriesPriceMapper;

  @GetMapping
  public ResponseEntity<List<CategoriesPriceDto>> readAll(@RequestParam @NotNull UUID supplierId) {
    Supplier supplier = supplierService.read(supplierId);

    return ResponseEntity.ok(
        supplier.getPreferences().getCategoriesPrices().stream()
            .map(categoriesPriceMapper::toDto)
            .toList());
  }
}
