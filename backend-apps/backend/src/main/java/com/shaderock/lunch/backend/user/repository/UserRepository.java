package com.shaderock.lunch.backend.user.repository;

import com.shaderock.lunch.backend.user.model.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {
  Optional<AppUser> findByUserDetails_Email(String email);

}
