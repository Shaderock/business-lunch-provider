package com.shaderock.lunch.backend.utils;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilterManager {

  public static final String SOFT_DELETE_FILTER = "softDeleteFilter";
  public static final String VISIBILITY_FILTER = "visibilityFilter";

  public static final String SOFT_DELETE_FILTER_PARAM_NAME = "isDeleted";
  public static final String VISIBILITY_FILTER_PARAM_NAME = "isPublic";

  private final EntityManager entityManager;

  public void switchSoftDeleteFilterToReturnNotDeleted() {
    switchBooleanFilter(SOFT_DELETE_FILTER, SOFT_DELETE_FILTER_PARAM_NAME, false);
  }

  public void switchVisibilityFilterToReturnPublic() {
    switchBooleanFilter(VISIBILITY_FILTER, VISIBILITY_FILTER_PARAM_NAME, true);
  }

  public void switchSoftDeleteFilterToReturnAll() {
    disableFilter(SOFT_DELETE_FILTER);
  }

  public void switchVisibilityFilterToReturnAll() {
    disableFilter(VISIBILITY_FILTER);
  }

  public void switchBooleanFilter(String filterName, String parameterName, boolean value) {
    entityManager
        .unwrap(Session.class)
        .enableFilter(filterName)
        .setParameter(parameterName, value);
    LOGGER.info("Filter [{}] enabled. [{}] = [{}]", filterName, parameterName, value);
  }

  public void disableFilter(String filterName) {
    entityManager
        .unwrap(Session.class)
        .disableFilter(filterName);
    LOGGER.info("Filter [{}] disabled", filterName);
  }
}
