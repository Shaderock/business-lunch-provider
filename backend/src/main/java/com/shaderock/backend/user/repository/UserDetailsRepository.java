package com.shaderock.backend.user.repository;

import com.shaderock.backend.user.model.entity.AppUserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDetailsRepository extends CrudRepository<AppUserDetails, Long> {
  Optional<AppUserDetails> findByEmail(String email);

  Optional<AppUserDetails> findByRegistrationToken(String token);
}
