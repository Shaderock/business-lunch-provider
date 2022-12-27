package com.shaderock.backend.repository.preference;

import com.shaderock.backend.model.entity.preference.SupplierPreferenceConfig;
import org.springframework.data.repository.CrudRepository;

public interface SupplierPreferenceRepository extends CrudRepository<SupplierPreferenceConfig, Long> {
}
