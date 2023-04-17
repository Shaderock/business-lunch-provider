package com.shaderock.lunch.backend.feature.user.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends DeletableEntityRepository<AppUser> {

}
