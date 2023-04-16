package com.shaderock.lunch.backend.user.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.user.model.entity.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends DeletableEntityRepository<AppUser> {

}
