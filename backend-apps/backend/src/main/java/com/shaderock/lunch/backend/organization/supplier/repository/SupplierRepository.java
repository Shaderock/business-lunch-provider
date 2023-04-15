package com.shaderock.lunch.backend.organization.supplier.repository;

import com.shaderock.lunch.backend.data.DeletableEntityRepository;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends DeletableEntityRepository<Supplier> {

  Optional<Supplier> findByIdAndIsPublicTrue(UUID id);

  Optional<Supplier> findByOrganizationDetails_Users_UserDetails_Email(String email);

}
