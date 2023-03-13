package com.shaderock.lunch.backend.organization.supplier.preference.repository;

import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface SupplierPreferencesRepository extends
    ListCrudRepository<SupplierPreferences, UUID> {

  Optional<SupplierPreferences> findBySupplier_OrganizationDetails_Users_UserDetails_Email(
      String email);

}
