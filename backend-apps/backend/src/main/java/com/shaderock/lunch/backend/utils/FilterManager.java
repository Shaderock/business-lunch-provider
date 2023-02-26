package com.shaderock.lunch.backend.utils;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterManager {

  public static final String DELETED_FILTER = "deletedFilter";
  private final EntityManager entityManager;

  public void enableDeleteFilter() {

    enableDeleteFilter(DELETED_FILTER);
  }

  public void disableDeleteFilter() {
    disableDeleteFilter(DELETED_FILTER);
  }

  public void enableDeleteFilter(String filterName) {
    entityManager
        .unwrap(Session.class)
        .enableFilter(filterName)
        .setParameter("isDeleted", true);
  }

  public void disableDeleteFilter(String filterName) {
    entityManager
        .unwrap(Session.class)
        .disableFilter(filterName);
  }

}
