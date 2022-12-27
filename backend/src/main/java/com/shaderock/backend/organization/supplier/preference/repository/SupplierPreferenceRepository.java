package com.shaderock.backend.organization.supplier.preference.repository;

import com.shaderock.backend.organization.supplier.preference.model.entity.SupplierPreferenceConfig;
import org.springframework.data.repository.CrudRepository;

public interface SupplierPreferenceRepository extends CrudRepository<SupplierPreferenceConfig, Long> {
}
