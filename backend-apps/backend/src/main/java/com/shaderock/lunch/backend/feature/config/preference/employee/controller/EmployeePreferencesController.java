package com.shaderock.lunch.backend.feature.config.preference.employee.controller;

import com.shaderock.lunch.backend.feature.config.preference.employee.dto.EmployeePreferencesDto;
import com.shaderock.lunch.backend.feature.config.preference.employee.entity.EmployeePreferences;
import com.shaderock.lunch.backend.feature.config.preference.employee.mapper.EmployeePreferencesMapper;
import com.shaderock.lunch.backend.feature.config.preference.employee.service.EmployeePreferencesService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.USER_EMPLOYEE_PREFERENCES)
public class EmployeePreferencesController {

  private final EmployeePreferencesMapper employeePreferencesMapper;
  private final EmployeePreferencesService employeePreferencesService;

  @GetMapping
  public ResponseEntity<EmployeePreferencesDto> read(Principal principal) {
    EmployeePreferences preferences = employeePreferencesService.read(principal);
    return ResponseEntity.ok(employeePreferencesMapper.toDto(preferences));
  }
}
