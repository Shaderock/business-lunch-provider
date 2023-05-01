package com.shaderock.lunch.backend.feature.food.category.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.config.preference.supplier.type.OrderType;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.supplier.entity.Supplier;
import com.shaderock.lunch.backend.feature.supplier.service.SupplierValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// todo disallow published to be updated
public class CategoryValidationService {

  private final SupplierValidationService supplierValidationService;

  // todo validate  Minimum required amount of categories ordered is [2] but only [1] sent
  public void validateCreate(Category category, Supplier supplier) {
    if (supplier.getPreferences().getOrderType() == OrderType.ONLY_ONE_OPTION_PER_CATEGORY) {
      long currentCategories = supplier.getMenu().getCategories().size();
      LOGGER.info("Current categories: {}", currentCategories);
      if (supplier.getPreferences()
          .getCategoriesPrices()
          .stream()
          .noneMatch(
              priceForCategories -> priceForCategories.getAmount() == currentCategories + 1)) {
        throw new CrudValidationException(String.format(
            "Can not create additional category while price for [%s] categories is not configured",
            currentCategories + 1));
      }
    }

    if (category.isPublic()) {
      supplierValidationService.validateCanCreatePublicCategories(supplier);
      validateCategoryCanBeMadePublic(category, supplier);
    }
  }

  public void validateCategoryCanBeMadePublic(Category category, Supplier supplier) {
    if (!supplier.isPublic() && category.isPublic()) {
      throw new CrudValidationException(
          String.format(
              "Can not set public Category(id=[%s]) because Supplier(id=[%s]) is not public",
              category.getId(), supplier.getId()));
    }
  }
}
