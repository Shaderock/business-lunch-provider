package com.shaderock.lunch.backend.feature.user.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends BaseEntityRepository<AppUser> {

}
