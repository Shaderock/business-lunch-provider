package com.shaderock.backend.supplier;

import com.shaderock.backend.model.entity.organization.Supplier;
import com.shaderock.backend.supplier.model.SupplierDTO;
import com.shaderock.backend.supplier.model.SupplierRegistrationForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplier")
public class SupplierController {
  private final SupplierService supplierService;

  @PostMapping("/register")
  public ResponseEntity<SupplierDTO> register(@RequestBody @Valid final SupplierRegistrationForm form,
                                              Principal principal) {
    Supplier registeredSupplier = supplierService.register(form, principal);
    SupplierDTO responseSupplier = supplierService.convertToDto(registeredSupplier);
    return ResponseEntity.ok(responseSupplier);
  }
}
