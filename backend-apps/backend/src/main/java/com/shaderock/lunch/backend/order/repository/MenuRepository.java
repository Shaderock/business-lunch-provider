package com.shaderock.lunch.backend.order.repository;

import com.shaderock.lunch.backend.order.model.entity.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {

}
