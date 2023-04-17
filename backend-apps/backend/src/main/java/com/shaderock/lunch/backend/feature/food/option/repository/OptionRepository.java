package com.shaderock.lunch.backend.feature.food.option.repository;

import com.shaderock.lunch.backend.data.repository.VisibleEntityRepository;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OptionRepository extends VisibleEntityRepository<Option> {

  List<Option> findByCategory_Menu_Supplier_Id(UUID id);

  List<Option> findByCategory_Id(UUID id);

  Optional<Option> findByIdAndCategory_Menu_Supplier_Id(UUID id, UUID supplierId);

  List<Option> findByCategoryAndCategory_Menu_Supplier(Category category, Supplier supplier);
}
