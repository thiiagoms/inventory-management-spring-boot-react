package io.thiiagoms.ims.shared.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfiguration {

  private static final RequestMatcher REGISTER_USER_ENDPOINT =
      PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/api/users");

  private static final RequestMatcher AUTHENTICATE_USER_ENDPOINT =
      PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/api/users/authenticate");

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(
            csrf ->
                csrf.ignoringRequestMatchers(REGISTER_USER_ENDPOINT, AUTHENTICATE_USER_ENDPOINT))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(REGISTER_USER_ENDPOINT, AUTHENTICATE_USER_ENDPOINT)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .build();
  }
}
