package com.shaderock.lunch.discovery.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf().ignoringRequestMatchers("/eureka/**")
        .and()
        .authorizeHttpRequests().anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .build();
  }
}
