package com.shaderock.backend.repository.user;

import com.shaderock.backend.model.entity.user.AppUserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDetailsRepository extends CrudRepository<AppUserDetails, Long> {
  Optional<AppUserDetails> findByEmail(String email);

  Optional<AppUserDetails> findByRegistrationToken(String token);
}
