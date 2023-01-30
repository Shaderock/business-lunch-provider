package com.shaderock.lunch.backend.order.repository;

import com.shaderock.lunch.backend.order.model.entity.Option;
import org.springframework.data.repository.CrudRepository;

public interface OptionRepository extends CrudRepository<Option, Long> {

}
