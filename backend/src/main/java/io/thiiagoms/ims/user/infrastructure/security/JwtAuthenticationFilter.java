package io.thiiagoms.ims.user.infrastructure.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public final class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  private final SecretKey signingKey;

  public JwtAuthenticationFilter(JwtProperties properties) {
    signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var token = bearerTokenFrom(request);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      authenticate(token);
    }

    filterChain.doFilter(request, response);
  }

  private String bearerTokenFrom(HttpServletRequest request) {
    var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
      return null;
    }

    var token = authorization.substring(BEARER_PREFIX.length()).trim();
    return token.isEmpty() ? null : token;
  }

  private void authenticate(String token) {
    try {
      var claims =
          Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
      var subject = claims.getSubject();
      var role = claims.get("role", String.class);

      if (subject == null || subject.isBlank() || role == null || role.isBlank()) {
        return;
      }

      var authorities = List.of(new SimpleGrantedAuthority("ROLE_%s".formatted(role)));
      var authentication =
          UsernamePasswordAuthenticationToken.authenticated(subject, token, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException | IllegalArgumentException exception) {
      SecurityContextHolder.clearContext();
    }
  }
}
