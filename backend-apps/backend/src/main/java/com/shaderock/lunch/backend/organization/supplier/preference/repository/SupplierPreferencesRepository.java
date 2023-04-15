package com.shaderock.lunch.backend.organization.supplier.preference.repository;

import com.shaderock.lunch.backend.data.DeletableEntityRepository;
import com.shaderock.lunch.backend.organization.supplier.preference.model.entity.SupplierPreferences;
import java.util.Optional;

public interface SupplierPreferencesRepository extends
    DeletableEntityRepository<SupplierPreferences> {

  Optional<SupplierPreferences> findBySupplier_OrganizationDetails_Users_UserDetails_Email(
      String email);

}
