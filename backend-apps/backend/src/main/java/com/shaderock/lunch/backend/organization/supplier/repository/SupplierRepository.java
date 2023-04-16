package com.shaderock.lunch.backend.organization.supplier.repository;

import com.shaderock.lunch.backend.data.repository.VisibleEntityRepository;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.Optional;

public interface SupplierRepository extends VisibleEntityRepository<Supplier> {

  Optional<Supplier> findByOrganizationDetails_Users_UserDetails_Email(String email);
}
