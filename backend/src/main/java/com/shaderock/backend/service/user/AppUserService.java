package com.shaderock.backend.service.user;

import com.shaderock.backend.model.entity.user.AppUser;
import com.shaderock.backend.repository.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
  private final AppUserRepository<AppUser> appUserRepository;

  public AppUser save(AppUser appUser) {
    return appUserRepository.save(appUser);
  }
}
