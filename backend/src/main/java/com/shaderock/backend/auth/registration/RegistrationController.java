package com.shaderock.backend.auth.registration;

import com.shaderock.backend.auth.registration.model.UserRegistrationForm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {
  private final RegistrationService registrationService;

  @Value(value = "${application.frontend.url}")
  private String frontendUrl;

  @GetMapping("/verify-user")
  public ResponseEntity<Boolean> isUserRegistered(@RequestParam @NotNull final String email) {
    return ResponseEntity.ok((registrationService.isUserRegistered(email)));
  }

  @PostMapping
  public ResponseEntity<Void> registerUser(@RequestBody @Valid final UserRegistrationForm form) {
    registrationService.registerUser(form);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/confirm-email")
  public ResponseEntity<Void> confirmEmail(@RequestParam @NotNull final String token) {
    registrationService.confirmEmail(token);
    return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", frontendUrl + "/login?emailConfirmed=true")
            .build();
  }
}
