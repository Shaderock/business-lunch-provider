package com.shaderock.backend.auth.login;

import com.shaderock.backend.auth.AuthService;
import com.shaderock.backend.auth.login.model.LoginForm;
import com.shaderock.backend.auth.login.model.UserDTO;
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
  public ResponseEntity<UserDTO> login(@RequestBody @Valid final LoginForm form) {
    return ResponseEntity.ok(authService.login(form));
  }
}
