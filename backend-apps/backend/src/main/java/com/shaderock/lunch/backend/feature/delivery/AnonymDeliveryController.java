package com.shaderock.lunch.backend.feature.delivery;

import com.shaderock.lunch.backend.feature.company.entity.Company;
import com.shaderock.lunch.backend.feature.company.service.CompanyService;
import com.shaderock.lunch.backend.feature.notification.entity.Notification;
import com.shaderock.lunch.backend.feature.notification.service.NotificationService;
import com.shaderock.lunch.backend.feature.user.entity.AppUser;
import com.shaderock.lunch.backend.util.ApiConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiConstants.ANONYM_DELIVERY)
public class AnonymDeliveryController {

  private final CompanyService companyService;
  private final NotificationService notificationService;

  @PostMapping("/deliver-lunch")
  @Transactional
  public ResponseEntity<Void> notifyLunchDelivered(@RequestParam @NotNull String supplierName,
      @RequestParam @NotNull UUID companyAppToken) {
    Company company = companyService.readByAppToken(companyAppToken);
    Notification notification = Notification.builder()
        .content(String.format("Lunch from %s has just been delivered", supplierName))
        .build();
    notificationService.create(notification,
        company.getOrganizationDetails().getUsers().stream()
            .map(AppUser::getUserDetails)
            .toList());

    return ResponseEntity.noContent().build();
  }
}
