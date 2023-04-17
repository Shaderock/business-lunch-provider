package com.shaderock.lunch.backend.feature.auth.login;

import com.shaderock.lunch.backend.feature.auth.AuthService;
import com.shaderock.lunch.backend.feature.auth.login.model.LoginForm;
import com.shaderock.lunch.backend.feature.auth.login.model.TokenResponse;
import com.shaderock.lunch.backend.util.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.LOGIN)
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping
  public ResponseEntity<TokenResponse> login(@RequestBody @Valid final LoginForm form) {
    String token = authService.login(form);
    return ResponseEntity.ok(new TokenResponse(token));
  }
}
