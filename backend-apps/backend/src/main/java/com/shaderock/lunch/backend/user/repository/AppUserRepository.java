package com.shaderock.lunch.backend.user.repository;

import com.shaderock.lunch.backend.user.model.entity.AppUser;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

  Optional<AppUser> findByUserDetails_Email(String email);

}
