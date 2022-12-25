package com.shaderock.backend.auth.registration;

import com.shaderock.backend.auth.registration.model.RegistrationForm;
import com.shaderock.backend.model.entity.user.AppUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  private final BCryptPasswordEncoder passwordEncoder;

  @Value(value = "${application.frontend.url}")
  private String frontendUrl;

  @GetMapping("/verify-user")
  public ResponseEntity<Boolean> isUserRegistered(@RequestParam @NotNull final String email) {
    return ResponseEntity.ok((registrationService.isUserRegistered(email)));
  }

  @PostMapping
  public ResponseEntity<Void> registerUser(@RequestBody @Valid RegistrationForm registrationForm) {
    AppUser appUser = AppUser.builder()
            .email(registrationForm.getEmail())
            .password(passwordEncoder.encode(registrationForm.getPassword()))
            .firstName(registrationForm.getFirstName())
            .lastName(registrationForm.getLastName())
            .build();

    registrationService.registerUser(appUser);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/confirm-email")
  public ResponseEntity<Void> confirmEmail(@RequestParam @NotNull String token) {
    registrationService.confirmEmail(token);
    return ResponseEntity.status(HttpStatus.FOUND).header("Location", frontendUrl + "/login?emailConfirmed=true").build();
  }
}
