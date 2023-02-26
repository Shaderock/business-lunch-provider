package com.shaderock.lunch.backend.organization.supplier.repository;

import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {

  Optional<Supplier> findByUsers_UserDetails_Email(String email);

  Optional<Supplier> findByName(String name);
}
