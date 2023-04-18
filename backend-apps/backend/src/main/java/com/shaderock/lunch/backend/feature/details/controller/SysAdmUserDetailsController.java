package com.shaderock.lunch.backend.feature.details.controller;

import com.shaderock.lunch.backend.feature.details.dto.AppUserDetailsDto;
import com.shaderock.lunch.backend.feature.details.entity.AppUserDetails;
import com.shaderock.lunch.backend.feature.details.mapper.AppUserDetailsMapper;
import com.shaderock.lunch.backend.feature.details.service.AppUserDetailsService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.SYS_ADM_USER_DETAILS)
public class SysAdmUserDetailsController {

  private final AppUserDetailsService userDetailsService;
  private final AppUserDetailsMapper userDetailsManager;

  @GetMapping("/all")
  public ResponseEntity<List<AppUserDetailsDto>> readUsersDetails() {
    List<AppUserDetails> details = userDetailsService.read();

    return ResponseEntity.ok(details.stream().map(userDetailsManager::toDto).toList());
  }
}
