package com.shaderock.lunch.backend.menu.repository;

import com.shaderock.lunch.backend.menu.model.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;


public interface CategoryRepository extends ListCrudRepository<Category, UUID> {

  List<Category> findByIsOrderingAllowedTrue();

  List<Category> findByMenu_Supplier_Id(UUID id);

  Optional<Category> findByIdAndMenu_Supplier_Id(UUID id, UUID id1);

  Optional<Category> findByNameAndMenu_Supplier_Id(String name, UUID id);

  Optional<Category> findByName(String name);
}
