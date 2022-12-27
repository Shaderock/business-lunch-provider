package com.shaderock.backend.auth.login;

import com.shaderock.backend.service.user.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final AppUserDetailsService appUserDetailsService;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token = header.substring(7);
    final Optional<String> optionalUsername = jwtTokenService.validateTokenAndGetUsername(token);
    if (optionalUsername.isEmpty()) {
      // validation failed or token expired
      filterChain.doFilter(request, response);
      return;
    }

    String username = optionalUsername.get();

    // set user details on spring security context
    final UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
    Collection<? extends GrantedAuthority> authorities = appUserDetailsService.getUserDetailsAuthorities(userDetails);
    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, authorities);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // continue with authenticated user
    filterChain.doFilter(request, response);
  }

}
