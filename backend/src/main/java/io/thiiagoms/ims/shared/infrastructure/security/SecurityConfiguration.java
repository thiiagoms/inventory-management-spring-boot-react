package io.thiiagoms.ims.shared.infrastructure.security;

import io.thiiagoms.ims.user.infrastructure.security.JwtAuthenticationFilter;
import io.thiiagoms.ims.user.infrastructure.security.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.HttpStatusAccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

  private static final RequestMatcher REGISTER_USER_ENDPOINT =
      PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/api/users");

  private static final RequestMatcher AUTHENTICATE_USER_ENDPOINT =
      PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/api/users/authenticate");

  private static final RequestMatcher HEALTH_ENDPOINT =
      PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/actuator/health");

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, JwtProperties jwtProperties)
      throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .formLogin(formLogin -> formLogin.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exceptions ->
                exceptions
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .accessDeniedHandler(new HttpStatusAccessDeniedHandler(HttpStatus.FORBIDDEN)))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        REGISTER_USER_ENDPOINT, AUTHENTICATE_USER_ENDPOINT, HEALTH_ENDPOINT)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtProperties), UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
