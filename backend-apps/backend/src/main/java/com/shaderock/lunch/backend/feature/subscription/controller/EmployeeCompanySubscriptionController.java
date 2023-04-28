package com.shaderock.lunch.backend.feature.subscription.controller;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.subscription.dto.SubscriptionDto;
import com.shaderock.lunch.backend.feature.subscription.entity.Subscription;
import com.shaderock.lunch.backend.feature.subscription.mapper.SubscriptionMapper;
import com.shaderock.lunch.backend.feature.subscription.service.SubscriptionService;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.EMPLOYEE_COMPANY_SUBSCRIPTION)
public class EmployeeCompanySubscriptionController {

  private final SubscriptionService subscriptionService;
  private final CompanyService companyService;
  private final SubscriptionMapper subscriptionMapper;

  @GetMapping("/all")
  @Transactional
  public ResponseEntity<List<SubscriptionDto>> read(Principal principal) {
    Company company = companyService.read(principal);
    List<Subscription> subscriptions = subscriptionService.read(company);
    return ResponseEntity.ok(subscriptions.stream().map(subscriptionMapper::toDto).toList());
  }
}
