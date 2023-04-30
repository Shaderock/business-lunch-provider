package com.shaderock.lunch.backend.feature.details.repository;

import com.shaderock.lunch.backend.data.repository.BaseEntityRepository;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import java.util.Optional;

public interface AppUserDetailsRepository extends BaseEntityRepository<AppUserDetails> {

  Optional<AppUserDetails> findByEmailAndIsEnabledTrue(String email);

  Optional<AppUserDetails> findByEmail(String email);

  Optional<AppUserDetails> findByRegistrationToken(String token);
}
