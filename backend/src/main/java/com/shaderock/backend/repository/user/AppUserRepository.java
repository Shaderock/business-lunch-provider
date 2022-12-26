package com.shaderock.backend.repository.user;

import com.shaderock.backend.model.entity.user.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository<U extends AppUser> extends UserRepository<U> {
}
