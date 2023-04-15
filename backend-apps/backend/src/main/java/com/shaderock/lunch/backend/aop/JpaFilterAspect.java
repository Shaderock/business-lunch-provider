package com.shaderock.lunch.backend.aop;

import com.shaderock.lunch.backend.menu.controller.SupplierCategoryController;
import com.shaderock.lunch.backend.utils.FilterManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class JpaFilterAspect {

  private final FilterManager filterManager;
  private final List<Class<?>> nonPublicControllers = List.of(SupplierCategoryController.class);

  @Before("within(@org.springframework.web.bind.annotation.RestController *)")
  public void setupFilters(JoinPoint joinPoint) {
    filterManager.switchSoftDeleteFilterToReturnNotDeleted();
    filterManager.switchVisibilityFilterToReturnPublic();

    if (nonPublicControllers.contains(joinPoint.getTarget().getClass())) {
      filterManager.switchVisibilityFilterToReturnAll();
    }
  }
}
