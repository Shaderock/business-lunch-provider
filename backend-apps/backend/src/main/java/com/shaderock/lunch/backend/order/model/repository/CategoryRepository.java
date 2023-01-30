package com.shaderock.lunch.backend.order.model.repository;

import com.shaderock.lunch.backend.order.model.entity.Category;
import java.util.Optional;
import org.hibernate.annotations.Where;
import org.springframework.data.repository.CrudRepository;


@Where(clause = "is_deleted=false AND supplier.is_deleted=false")
public interface CategoryRepository extends CrudRepository<Category, Long> {

  Optional<Category> findByName(String name);
}
