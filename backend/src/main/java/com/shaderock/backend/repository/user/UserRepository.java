package com.shaderock.backend.repository.user;

import com.shaderock.backend.model.entity.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserRepository<U extends AppUser> extends JpaRepository<U, Long> {
  Optional<U> findByEmail(String email);

  Optional<U> findByRegistrationToken(String email);
}
