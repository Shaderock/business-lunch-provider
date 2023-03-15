package com.shaderock.lunch.backend.menu.repository;

import com.shaderock.lunch.backend.menu.model.entity.Category;
import com.shaderock.lunch.backend.menu.model.entity.Option;
import com.shaderock.lunch.backend.organization.supplier.model.entity.Supplier;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface OptionRepository extends ListCrudRepository<Option, UUID> {

  List<Option> findByIsPublicTrueAndCategoryAndCategory_Menu_Supplier(Category category,
      Supplier supplier);

  List<Option> findByIsPublicTrueAndCategory_Menu_Supplier_Id(UUID id);

  List<Option> findByCategory_Menu_Supplier_Id(UUID id);

  Optional<Option> findByIdAndIsPublicTrue(UUID id);

  List<Option> findByCategory_Id(UUID id);

  Optional<Option> findByIdAndCategory_Menu_Supplier_Id(UUID id, UUID id1);
}
