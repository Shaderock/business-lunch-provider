package com.shaderock.lunch.backend.feature.food.option.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.food.category.entity.Category;
import com.shaderock.lunch.backend.feature.food.option.entity.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionValidationService {

  public void validateOptionCanBeMadePublic(Option option, Category category) {
    if (!category.isPublic() && option.isPublic()) {
      throw new CrudValidationException(
          String.format(
              "Can not set public Option(id=[%s]) because Category(id=[%s]) is not public",
              category.getId(), category.getId()));
    }
  }
}
