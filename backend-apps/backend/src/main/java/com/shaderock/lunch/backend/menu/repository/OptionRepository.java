package com.shaderock.lunch.backend.menu.repository;

import com.shaderock.lunch.backend.menu.model.entity.Option;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface OptionRepository extends ListCrudRepository<Option, UUID> {

  Optional<Option> findByName(String name);
}
