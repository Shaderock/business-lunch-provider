package com.shaderock.backend.conf;

import com.shaderock.backend.auth.login.JWTRequestFilter;
import com.shaderock.backend.model.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class BasicConfiguration {
  private final JWTRequestFilter jwtRequestFilter;

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
          throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.cors().and().csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/login/**", "/api/register/**", "/api/food/**").permitAll()
            .anyRequest().hasRole(Role.USER.getName())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
