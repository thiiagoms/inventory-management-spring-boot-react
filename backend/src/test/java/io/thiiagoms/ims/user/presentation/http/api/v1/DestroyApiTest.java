package io.thiiagoms.ims.user.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.shared.presentation.http.ApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.UserApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register.RegisterUserRequestBuilder;
import io.thiiagoms.ims.user.infrastructure.persistence.repository.UserJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class DestroyApiTest extends ApiTestSupport {

  private final UserApiTestSupport apiTestSupport;

  private final UserJpaRepository repository;

  @Autowired
  public DestroyApiTest(MockMvc mockMvc, UserJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
    this.apiTestSupport = new UserApiTestSupport(this);
  }

  @Test
  void itAllowsAnAuthenticatedUserToDeleteTheirProfile() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();

    deleteJson(endpointFor(authenticatedUser.id()), authenticatedUser.accessToken())
        .andExpect(status().isNoContent());

    assertThat(repository.findById(UUID.fromString(authenticatedUser.id()))).isEmpty();
  }

  @Test
  void itRejectsADeleteWithoutAuthentication() throws Exception {
    var userId = apiTestSupport.createUserAndReturnId();

    deleteJson(endpointFor(userId)).andExpect(status().isUnauthorized());

    assertThat(repository.findById(UUID.fromString(userId))).isPresent();
  }

  @Test
  void itRejectsADeleteWithAnInvalidToken() throws Exception {
    var userId = apiTestSupport.createUserAndReturnId();

    deleteJson(endpointFor(userId), "not-a-valid-jwt").andExpect(status().isUnauthorized());

    assertThat(repository.findById(UUID.fromString(userId))).isPresent();
  }

  @Test
  void itRejectsADeleteOfAnotherUsersProfile() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var anotherUser =
        RegisterUserRequestBuilder.start()
            .withEmail("another.user@example.com")
            .withPhone("21999999999")
            .build();
    var anotherUserId = apiTestSupport.createUserAndReturnId(anotherUser);

    deleteJson(endpointFor(anotherUserId), authenticatedUser.accessToken())
        .andExpect(status().isForbidden());

    assertThat(repository.findById(UUID.fromString(anotherUserId))).isPresent();
  }

  @Test
  void itReturnsNotFoundWhenTheAuthenticatedProfileDoesNotExist() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    repository.deleteById(UUID.fromString(authenticatedUser.id()));

    deleteJson(endpointFor(authenticatedUser.id()), authenticatedUser.accessToken())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("resource_not_found"))
        .andExpect(jsonPath("$.field").value("id"))
        .andExpect(jsonPath("$.message").value("User not found with the provided id."));
  }

  private AuthenticatedUser registerAndAuthenticateUser() throws Exception {
    var registration = RegisterUserRequestBuilder.start().build();
    var userId = apiTestSupport.createUserAndReturnId(registration);
    var accessToken = apiTestSupport.authenticateAndReturnToken(registration);
    return new AuthenticatedUser(userId, accessToken);
  }

  private String endpointFor(String userId) {
    return UserApiTestSupport.ENDPOINT + "/" + userId;
  }

  private record AuthenticatedUser(String id, String accessToken) {}
}
