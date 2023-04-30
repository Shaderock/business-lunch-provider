package com.shaderock.lunch.backend.feature.supplier.controller;

import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.feature.supplier.dto.SupplierDto;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.mapper.SupplierMapper;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SUPPLIER_ADM_SUPPLIER)
// todo when updating isPublic, make sure that there are no subscribers
public class SupplierAdmSupplierController {

  private final SupplierService supplierService;
  private final SupplierMapper supplierMapper;
  private final AppUserDetailsService userDetailsService;

  @GetMapping("/my")
  public ResponseEntity<SupplierDto> read(Principal principal) {
    Supplier supplier = supplierService.read(principal);
    return ResponseEntity.ok(supplierMapper.toDto(supplier));
  }

  @PutMapping
  @Transactional
  public ResponseEntity<SupplierDto> update(@RequestBody @Valid @NotNull SupplierDto supplierDto,
      Principal principal) {
    Supplier supplier = supplierService.read(principal);
    AppUserDetails userDetails = userDetailsService.read(principal);
    supplierService.update(supplierDto, userDetails);
    return ResponseEntity.ok(supplierMapper.toDto(supplier));
  }
}
