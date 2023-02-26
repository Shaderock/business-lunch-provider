package com.shaderock.lunch.backend.organization.supplier.preference.repository;

import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import org.springframework.data.repository.ListCrudRepository;

public interface SupplierPreferencesRepository extends
    ListCrudRepository<SupplierPreferences, Long> {

}
