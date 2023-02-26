package com.shaderock.lunch.backend.user.repository;

import com.shaderock.lunch.backend.user.model.entity.AppUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends ListCrudRepository<AppUser, Long> {

}
