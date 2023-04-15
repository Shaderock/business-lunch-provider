package com.shaderock.lunch.backend.data;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DeletableEntityRepository<T extends DeletableEntity> extends
    BaseEntityRepository<T> {

}
