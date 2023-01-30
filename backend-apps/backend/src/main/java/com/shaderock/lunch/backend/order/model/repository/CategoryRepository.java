package com.shaderock.lunch.backend.order.model.repository;

import com.shaderock.lunch.backend.order.model.entity.Category;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Long> {

  Optional<Category> findByName(String name);
}
