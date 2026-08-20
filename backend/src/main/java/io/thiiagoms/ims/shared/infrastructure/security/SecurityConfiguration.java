package io.thiiagoms.ims.shared.infrastructure.security;

import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.pathPattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfiguration {

  private static final RequestMatcher REGISTER_USER_ENDPOINT =
      pathPattern(HttpMethod.POST, "/api/users");

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.ignoringRequestMatchers(REGISTER_USER_ENDPOINT))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(REGISTER_USER_ENDPOINT)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .build();
  }
}
