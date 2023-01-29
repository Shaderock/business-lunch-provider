package com.shaderock.lunch.backend.user.repository;

import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDetailsRepository extends CrudRepository<AppUserDetails, Long> {
  Optional<AppUserDetails> findByEmail(String email);

  Optional<AppUserDetails> findByRegistrationToken(String token);
}
