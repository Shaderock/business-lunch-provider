package com.shaderock.lunch.backend.auth.login;

import com.shaderock.lunch.backend.auth.AuthService;
import com.shaderock.lunch.backend.auth.login.model.LoginForm;
import com.shaderock.lunch.backend.auth.login.model.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping
  public ResponseEntity<TokenResponse> login(@RequestBody @Valid final LoginForm form) {
    String token = authService.login(form);
    return ResponseEntity.ok(new TokenResponse(token));
  }
}
