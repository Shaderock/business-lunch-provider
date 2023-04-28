package com.shaderock.lunch.backend.feature.food.price.service;

import com.shaderock.lunch.backend.feature.food.price.entity.PriceForCategories;
import com.shaderock.lunch.backend.feature.food.price.repository.PriceForCategoriesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceForCategoriesService {

  private final PriceForCategoriesRepository priceForCategoriesRepository;

  @Transactional
  public PriceForCategories create(PriceForCategories priceForCategories) {
    return priceForCategoriesRepository.save(priceForCategories);
  }
}
