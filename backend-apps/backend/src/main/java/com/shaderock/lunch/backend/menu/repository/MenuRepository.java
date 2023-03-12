package com.shaderock.lunch.backend.menu.repository;

import com.shaderock.lunch.backend.menu.model.entity.Menu;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface MenuRepository extends ListCrudRepository<Menu, UUID> {

  Optional<Menu> findBySupplier(Supplier supplier);
}
