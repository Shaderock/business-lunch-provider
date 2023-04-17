package com.shaderock.lunch.backend.feature.food.category.repository;

import com.shaderock.lunch.backend.data.repository.VisibleEntityRepository;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CategoryRepository extends VisibleEntityRepository<Category> {

  List<Category> findByMenu_Supplier_Id(UUID id);

  Optional<Category> findByIdAndMenu_Supplier_Id(UUID id, UUID id1);

  Optional<Category> findByNameAndMenu_Supplier_Id(String name, UUID id);
}
