package com.shaderock.lunch.backend.order.repository;

import com.shaderock.lunch.backend.order.model.entity.Menu;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {

  Optional<Menu> findBySupplier(Supplier supplier);
}
