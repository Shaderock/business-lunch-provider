package com.shaderock.lunch.backend.organization.supplier.preference.repository;

import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface SupplierPreferencesRepository extends
    ListCrudRepository<SupplierPreferences, UUID> {

}
