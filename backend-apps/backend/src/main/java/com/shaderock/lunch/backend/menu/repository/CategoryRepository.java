package com.shaderock.lunch.backend.menu.repository;

import com.shaderock.lunch.backend.data.VisibleEntityRepository;
import com.shaderock.lunch.backend.menu.model.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CategoryRepository extends VisibleEntityRepository<Category> {

  List<Category> findByMenu_Supplier_Id(UUID id);

  Optional<Category> findByIdAndMenu_Supplier_Id(UUID id, UUID id1);

  Optional<Category> findByNameAndMenu_Supplier_Id(String name, UUID id);
}
