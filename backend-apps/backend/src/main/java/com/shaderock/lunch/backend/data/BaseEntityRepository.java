package com.shaderock.lunch.backend.data;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity> extends ListCrudRepository<T, UUID> {

}
