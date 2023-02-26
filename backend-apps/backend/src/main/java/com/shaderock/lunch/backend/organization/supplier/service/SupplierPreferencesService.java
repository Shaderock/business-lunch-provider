package com.shaderock.lunch.backend.organization.supplier.service;

import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import com.shaderock.lunch.backend.organization.supplier.preference.repository.SupplierPreferencesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierPreferencesService {

  private final SupplierPreferencesRepository supplierPreferencesRepository;

  @Transactional
  public SupplierPreferences create(SupplierPreferences preferences) {
    // todo validate
    return supplierPreferencesRepository.save(preferences);
  }

  public void delete(SupplierPreferences preferences) {
    // todo call read and validate
    supplierPreferencesRepository.delete(preferences);
  }
}
