package com.shaderock.lunch.backend.feature.details.service;

import com.shaderock.lunch.backend.communication.exception.CrudValidationException;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.repository.AppUserDetailsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsValidationService {

  private final AppUserDetailsRepository appUserDetailsRepository;

  public void validateCreate(@NonNull AppUserDetails details) {
    if (appUserDetailsRepository.findByEmail(details.getEmail()).isPresent()) {
      throw new CrudValidationException(String.format("User(email=[%s]) already exists", details));
    }
  }
}
