package com.shaderock.lunch.backend.data.repository;

import com.shaderock.lunch.backend.data.entity.BaseEntity;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity> extends ListCrudRepository<T, UUID> {

}
