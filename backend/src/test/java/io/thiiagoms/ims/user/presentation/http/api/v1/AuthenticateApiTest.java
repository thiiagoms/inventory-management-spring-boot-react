package io.thiiagoms.ims.user.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.shared.presentation.http.ApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.UserApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register.RegisterUserRequestBuilder;
import io.thiiagoms.ims.user.infrastructure.persistence.repository.UserJpaRepository;
import io.thiiagoms.ims.user.presentation.http.api.v1.auth.AuthenticateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

public class AuthenticateApiTest extends ApiTestSupport {

  private static final String ENDPOINT = UserApiTestSupport.ENDPOINT + "/authenticate";

  private final UserApiTestSupport apiTestSupport;

  private final UserJpaRepository repository;

  @Autowired
  public AuthenticateApiTest(MockMvc mockMvc, UserJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
    this.apiTestSupport = new UserApiTestSupport(this);
  }

  @Test
  void itAuthenticatesTheUserAndRecordsTheLastLoginTimestamp() throws Exception {
    var registeredUser = RegisterUserRequestBuilder.start().build();
    var userId = apiTestSupport.createUserAndReturnId(registeredUser);
    var request = new AuthenticateRequest(registeredUser.email(), registeredUser.password());

    postJson(ENDPOINT, request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.expiresAt").isNotEmpty());

    var persistedUser = repository.findById(java.util.UUID.fromString(userId)).orElseThrow();
    assertThat(persistedUser.getLastLoginAt()).isNotNull();
  }

  @Test
  void itRejectsAuthenticationWhenTheEmailDoesNotExist() throws Exception {
    var request = new AuthenticateRequest("missing@example.com", "Strong@123");
    assertInvalidCredentials(request);
  }

  @Test
  void itRejectsAuthenticationWhenThePasswordDoesNotMatch() throws Exception {
    var registeredUser = RegisterUserRequestBuilder.start().build();
    apiTestSupport.createUserAndReturnId(registeredUser);
    var request = new AuthenticateRequest(registeredUser.email(), "Different@123");

    assertInvalidCredentials(request);
  }

  @Test
  void itValidatesBlankAuthenticationFields() throws Exception {
    var request = new AuthenticateRequest("", "");

    postJson(ENDPOINT, request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").exists())
        .andExpect(jsonPath("$.message").value("must not be blank"));
  }

  @Test
  void itValidatesMalformedEmail() throws Exception {
    var request = new AuthenticateRequest("invalid-email", "Strong@123");

    postJson(ENDPOINT, request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("email"))
        .andExpect(jsonPath("$.message").value("Invalid e-mail address."));
  }

  private void assertInvalidCredentials(AuthenticateRequest request) throws Exception {
    postJson(ENDPOINT, request)
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
        .andExpect(jsonPath("$.error").value("authorization_failed"))
        .andExpect(jsonPath("$.field").value("credentials"))
        .andExpect(jsonPath("$.message").value("Invalid e-mail or password."));
  }
}
