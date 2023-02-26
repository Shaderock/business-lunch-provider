package com.shaderock.lunch.backend.organization.supplier.repository;

import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface SupplierRepository extends ListCrudRepository<Supplier, UUID> {

  Optional<Supplier> findByOrganizationDetails_Users_UserDetails_Email(String email);

}
