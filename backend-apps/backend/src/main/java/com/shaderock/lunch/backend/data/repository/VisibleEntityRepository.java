package com.shaderock.lunch.backend.data.repository;

import com.shaderock.lunch.backend.data.entity.VisibleEntity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface VisibleEntityRepository<T extends VisibleEntity> extends
    DeletableEntityRepository<T> {

}
