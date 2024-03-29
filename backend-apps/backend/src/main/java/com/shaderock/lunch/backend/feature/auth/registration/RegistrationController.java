package com.shaderock.lunch.backend.feature.auth.registration;

import com.shaderock.lunch.backend.feature.auth.AuthService;
import com.shaderock.lunch.backend.feature.auth.registration.model.UserRegistrationForm;
import com.shaderock.lunch.backend.util.ApiConstants;
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
@RequestMapping(ApiConstants.REGISTER)
@RequiredArgsConstructor
public class RegistrationController {

  private final AuthService authService;

  @Value(value = "${lunch.frontend.url}")
  private String frontendUrl;

  @GetMapping("/verify-user")
  public ResponseEntity<Boolean> isUserEmailValid(@RequestParam @NotNull final String email) {
    return ResponseEntity.ok((!authService.isUserRegistered(email)));
  }

  @PostMapping
  public ResponseEntity<Void> registerUser(@RequestBody @Valid final UserRegistrationForm form) {
    authService.registerUser(form);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/confirm-email")
  public ResponseEntity<Void> confirmEmail(@RequestParam @NotNull final String token) {
    authService.confirmEmail(token);
    return ResponseEntity.status(HttpStatus.FOUND)
        .header("Location", frontendUrl + "/login?emailConfirmed=true")
        .build();
  }
}
