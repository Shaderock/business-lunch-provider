package com.shaderock.lunch.backend.user.repository;

import com.shaderock.lunch.backend.user.model.entity.AppUserDetails;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

public interface AppUserDetailsRepository extends ListCrudRepository<AppUserDetails, Long> {

  Optional<AppUserDetails> findByEmail(String email);

  Optional<AppUserDetails> findByRegistrationToken(String token);
}
