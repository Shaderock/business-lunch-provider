package com.shaderock.lunch.backend.menu.repository;

import com.shaderock.lunch.backend.menu.model.entity.Category;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;


public interface CategoryRepository extends ListCrudRepository<Category, Long> {

  Optional<Category> findByName(String name);
}
