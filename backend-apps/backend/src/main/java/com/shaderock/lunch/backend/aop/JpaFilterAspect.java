package com.shaderock.lunch.backend.aop;

import com.shaderock.lunch.backend.feature.company.controller.PrivilegedCompanyController;
import com.shaderock.lunch.backend.feature.config.preference.supplier.controller.SupplierPreferencesController;
import com.shaderock.lunch.backend.feature.food.category.controller.SupplierCategoryController;
import com.shaderock.lunch.backend.feature.food.option.controller.SupplierOptionController;
import com.shaderock.lunch.backend.feature.organization.controller.PrivilegedOrganizationController;
import com.shaderock.lunch.backend.feature.supplier.controller.PrivilegedSupplierController;
import com.shaderock.lunch.backend.feature.user.controller.PrivilegedUserController;
import com.shaderock.lunch.backend.util.FilterManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class JpaFilterAspect {

  private final FilterManager filterManager;
  private final List<Class<?>> ignoreVisibilityControllers = List.of(
      SupplierCategoryController.class,
      SupplierOptionController.class,
      SupplierPreferencesController.class);
  private final List<Class<?>> ignoreDeletedControllers = List.of();

  private final List<Class<?>> ignoreFiltersControllers = List.of(
      PrivilegedCompanyController.class,
      PrivilegedOrganizationController.class,
      PrivilegedSupplierController.class,
      PrivilegedUserController.class
  );

  @Before("within(@org.springframework.web.bind.annotation.RestController *)")
  public void setupFilters(JoinPoint joinPoint) {
    filterManager.returnNotDeleted();
    filterManager.returnPublic();

    if (ignoreFiltersControllers.contains(joinPoint.getTarget().getClass())) {
      filterManager.ignoreDeleted();
      filterManager.ignoreVisibility();
    }

    if (ignoreVisibilityControllers.contains(joinPoint.getTarget().getClass())) {
      filterManager.ignoreVisibility();
    }

    if (ignoreDeletedControllers.contains(joinPoint.getTarget().getClass())) {
      filterManager.ignoreDeleted();
    }
  }
}
