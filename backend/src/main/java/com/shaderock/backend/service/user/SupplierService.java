package com.shaderock.backend.service.user;

import com.shaderock.backend.model.entity.user.Supplier;
import com.shaderock.backend.repository.user.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {
  private final SupplierRepository<Supplier> supplierRepository;

  public Supplier save(Supplier supplier) {
    return supplierRepository.save(supplier);
  }
}
