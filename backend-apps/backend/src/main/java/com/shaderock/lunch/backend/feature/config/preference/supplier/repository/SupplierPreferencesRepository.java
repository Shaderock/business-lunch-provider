package com.shaderock.lunch.backend.feature.config.preference.supplier.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.config.preference.supplier.entity.SupplierPreferences;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.Optional;

public interface SupplierPreferencesRepository extends
    DeletableEntityRepository<SupplierPreferences> {

  Optional<SupplierPreferences> findBySupplier(Supplier supplier);

  Optional<SupplierPreferences> findBySupplier_OrganizationDetails_Users_UserDetails_Email(
      String email);

}
