package com.shaderock.lunch.backend.conf;

import static org.springframework.security.config.Customizer.withDefaults;

import com.shaderock.lunch.backend.feature.auth.login.JWTRequestFilter;
import com.shaderock.lunch.backend.feature.details.type.Role;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

  private final JWTRequestFilter jwtRequestFilter;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  @Value(value = "${lunch.backend.actuator.username}")
  String actuatorUser;
  @Value(value = "${lunch.backend.actuator.password}")
  String actuatorPassword;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.cors(withDefaults()).csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/swagger-ui.html", "/swagger-ui/**",
            "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
        .requestMatchers("/api/login/**", "/api/register/**", "/api/food/**").permitAll()
        .requestMatchers("/api/anonym/**").permitAll()
        .requestMatchers("/api/sys-adm/**").hasRole(Role.SYSTEM_ADMIN.getName())
        .requestMatchers("/api/organization-adm/**")
        .hasAnyRole(Role.COMPANY_ADMIN.getName(), Role.SUPPLIER.getName())
        .requestMatchers("/api/supplier-adm/**").hasRole(Role.SUPPLIER.getName())
        .requestMatchers("/api/company-adm/**").hasRole(Role.COMPANY_ADMIN.getName())
        .requestMatchers("/api/employee/**").hasRole(Role.EMPLOYEE.getName())
        .requestMatchers("/actuator/**").authenticated().and().httpBasic()
        .and()
        .authorizeHttpRequests()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(actuatorUser)
        .password(bCryptPasswordEncoder.encode(actuatorPassword)).roles("ACTUATOR");
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
    configuration.setAllowCredentials(true);

    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
