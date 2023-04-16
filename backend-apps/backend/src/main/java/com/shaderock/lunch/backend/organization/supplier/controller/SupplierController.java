package com.shaderock.lunch.backend.organization.supplier.controller;

import com.shaderock.lunch.backend.organization.supplier.mapper.SupplierMapper;
import com.shaderock.lunch.backend.organization.supplier.model.dto.SupplierDto;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import com.shaderock.lunch.backend.organization.supplier.model.form.OrganizationRegistrationForm;
import com.shaderock.lunch.backend.organization.supplier.service.SupplierService;
import com.shaderock.lunch.backend.user.AppUserDetailsService;
import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import com.shaderock.lunch.backend.utils.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SUPPLIER)
public class SupplierController {

  private final SupplierService supplierService;
  private final SupplierMapper supplierMapper;
  private final AppUserDetailsService userDetailsService;

  @PostMapping("/register")
  public ResponseEntity<SupplierDto> register(
      @RequestBody @Valid final OrganizationRegistrationForm form, Principal principal) {
    AppUserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    Supplier registeredSupplier = supplierService.register(form, userDetails);
    SupplierDto responseSupplier = supplierMapper.toDto(registeredSupplier);
    return ResponseEntity.ok(responseSupplier);
  }

  @GetMapping("/all")
  public ResponseEntity<List<SupplierDto>> readAll() {
    List<Supplier> suppliers = supplierService.read();

    return ResponseEntity.ok(suppliers.stream().map(supplierMapper::toDto).toList());
  }
}
