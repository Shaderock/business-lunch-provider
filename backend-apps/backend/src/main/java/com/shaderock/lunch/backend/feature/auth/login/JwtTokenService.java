package com.shaderock.lunch.backend.feature.auth.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtTokenService {

  private final Algorithm hmac512;
  private final JWTVerifier verifier;

  public JwtTokenService(@Value("${lunch.jwt.secret}") final String secret) {
    this.hmac512 = Algorithm.HMAC512(secret);
    this.verifier = JWT.require(this.hmac512).build();
  }

  public String generateToken(final UserDetails userDetails) {
    LocalDate date = LocalDate.now().plusMonths(1);

    ZoneId zoneId = ZoneId.systemDefault();
    Instant instant = date.atStartOfDay(zoneId).toInstant();

    LOGGER.info("Generating token for username=[{}] expiring at [{}]",
        userDetails.getUsername(), date);

    return JWT.create()
        .withSubject(userDetails.getUsername())
        .withExpiresAt(instant)
        .sign(this.hmac512);
  }

  public Optional<String> validateTokenAndGetUsername(final String token) {
    try {
      return Optional.of(verifier.verify(token).getSubject());
    } catch (final JWTVerificationException verificationEx) {
      return Optional.empty();
    }
  }
}
