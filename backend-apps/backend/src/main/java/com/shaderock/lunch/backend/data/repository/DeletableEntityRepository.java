package com.shaderock.lunch.backend.data.repository;

import com.shaderock.lunch.backend.data.entity.DeletableEntity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DeletableEntityRepository<T extends DeletableEntity> extends
    BaseEntityRepository<T> {

}
