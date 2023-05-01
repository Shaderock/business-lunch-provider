package com.shaderock.lunch.backend.feature.food.price.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.food.price.entity.CategoriesPrice;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import java.util.Optional;
import java.util.UUID;

public interface CategoriesPriceRepository extends BaseEntityRepository<CategoriesPrice> {


  Optional<CategoriesPrice> findByIdAndSupplierPreferences_Supplier(UUID id, Supplier supplier);
}
