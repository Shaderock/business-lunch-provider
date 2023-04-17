package com.shaderock.lunch.backend.feature.user.repository;

import com.shaderock.lunch.backend.data.repository.DeletableEntityRepository;
import com.shaderock.lunch.backend.feature.user.entity.AppUserDetails;
import java.util.Optional;

public interface AppUserDetailsRepository extends DeletableEntityRepository<AppUserDetails> {

  Optional<AppUserDetails> findByEmail(String email);

  Optional<AppUserDetails> findByRegistrationToken(String token);
}
