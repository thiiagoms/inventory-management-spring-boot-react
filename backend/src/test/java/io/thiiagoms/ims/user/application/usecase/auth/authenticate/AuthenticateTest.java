package io.thiiagoms.ims.user.application.usecase.auth.authenticate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository.UserMemoryRepository;
import io.thiiagoms.ims.shared.domain.time.Clock;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.user.application.exception.InvalidCredentialsException;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.AuthenticationToken;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.security.TokenIssuer;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Token;
import io.thiiagoms.ims.user.domain.valueobject.TokenExpiresAt;
import io.thiiagoms.ims.user.domain.valueobject.TokenIssuedAt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticateTest {

  private static final Timestamp AUTHENTICATED_AT = new Timestamp("2026-08-21T12:00:00Z");

  @Mock private TokenIssuer tokenIssuer;

  @Mock private PasswordEncoder encoder;

  @Mock private Clock clock;

  private UserRepository repository;

  private AuthenticateData data;

  private Authenticate useCase;

  @BeforeEach
  void setUp() {
    repository = new UserMemoryRepository();

    data =
        new AuthenticateData(new Email("ilovelaravel@gmail.com"), new PasswordPlain("P4ssword!"));

    useCase = new Authenticate(new UserFinder(repository), tokenIssuer, repository, encoder, clock);
  }

  @Test
  void itAuthenticatesTheUserAndRecordsTheLoginTimestamp() {
    var user = UserFake.start().withEmail(data.email()).build();
    repository.save(user);

    var issuedAt = new TokenIssuedAt(AUTHENTICATED_AT);
    var token =
        new AuthenticationToken(
            new Token("signed-token"), new TokenExpiresAt(new Timestamp("2026-08-21T13:00:00Z")));

    when(encoder.matches(data.password(), user.password())).thenReturn(true);
    when(clock.now()).thenReturn(AUTHENTICATED_AT);
    when(tokenIssuer.issueFor(user, issuedAt)).thenReturn(token);

    var authData = useCase.execute(data);

    assertEquals(token.token().value(), authData.token());
    assertEquals(token.expiresAt().value().value(), authData.expiresAt());
    assertEquals(
        issuedAt.value(), repository.findById(user.id()).orElseThrow().lastLoginAt().orElseThrow());
  }

  @Test
  void itRejectsAuthenticationWhenTheUserEmailDoesNotExist() {
    InvalidCredentialsException exception =
        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(data));

    assertEquals(InvalidCredentialsException.FIELD, exception.getField());
    assertEquals("Invalid e-mail or password.", exception.getMessage());

    assertTrue(repository.findByEmail(data.email()).isEmpty());
    verify(encoder, never()).matches(any(), any());
    verify(clock, never()).now();
    verify(tokenIssuer, never()).issueFor(any(), any());
  }

  @Test
  void itRejectsAuthenticationWhenTheEmailExistsButThePasswordDoesNotMatch() {

    var user = UserFake.start().withEmail(data.email()).build();
    repository.save(user);

    when(encoder.matches(data.password(), user.password())).thenReturn(false);

    InvalidCredentialsException exception =
        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(data));

    assertEquals(InvalidCredentialsException.FIELD, exception.getField());
    assertEquals("Invalid e-mail or password.", exception.getMessage());

    assertTrue(user.lastLoginAt().isEmpty());
    verify(clock, never()).now();
    verify(tokenIssuer, never()).issueFor(any(), any());
  }
}
