package com.shaderock.lunch.backend.data;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface VisibleEntityRepository<T extends VisibleEntity> extends
    DeletableEntityRepository<T> {

}
