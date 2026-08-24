package io.thiiagoms.ims.user.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.shared.presentation.http.ApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.UserApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register.RegisterUserRequestBuilder;
import io.thiiagoms.ims.user.infrastructure.persistence.model.UserJpa;
import io.thiiagoms.ims.user.infrastructure.persistence.repository.UserJpaRepository;
import io.thiiagoms.ims.user.presentation.http.api.v1.register.RegisterRequest;
import io.thiiagoms.ims.user.presentation.http.api.v1.update.UpdateRequest;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class UpdateApiTest extends ApiTestSupport {

  private final UserApiTestSupport apiTestSupport;

  private final UserJpaRepository repository;

  @Autowired
  public UpdateApiTest(MockMvc mockMvc, UserJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
    this.apiTestSupport = new UserApiTestSupport(this);
  }

  @Test
  void itAllowsAnAuthenticatedUserToUpdateTheirEntireProfile() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var request =
        new UpdateRequest(
            "Jessica Jones", "jessica.jones@example.com", "N3wStrong@123", "21988887777");

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(authenticatedUser.id()))
        .andExpect(jsonPath("$.name").value(request.name()))
        .andExpect(jsonPath("$.email").value(request.email()))
        .andExpect(jsonPath("$.phone").value(request.phone()));

    var persistedUser = findUser(authenticatedUser.id());
    assertThat(persistedUser.getName()).isEqualTo(request.name());
    assertThat(persistedUser.getEmail()).isEqualTo(request.email());
    assertThat(persistedUser.getPhone()).isEqualTo(request.phone());
    assertThat(persistedUser.getPassword()).isNotEqualTo(request.password()).startsWith("$2");
  }

  @Test
  void itUpdatesOnlyTheUserName() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var passwordBeforeUpdate = findUser(authenticatedUser.id()).getPassword();
    var request = new UpdateRequest("Jessica Jones", null, null, null);

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(request.name()))
        .andExpect(jsonPath("$.email").value(authenticatedUser.registration().email()))
        .andExpect(jsonPath("$.phone").value(authenticatedUser.registration().phone()));

    var persistedUser = findUser(authenticatedUser.id());
    assertThat(persistedUser.getName()).isEqualTo(request.name());
    assertThat(persistedUser.getEmail()).isEqualTo(authenticatedUser.registration().email());
    assertThat(persistedUser.getPhone()).isEqualTo(authenticatedUser.registration().phone());
    assertThat(persistedUser.getPassword()).isEqualTo(passwordBeforeUpdate);
  }

  @Test
  void itUpdatesOnlyTheUserEmail() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var passwordBeforeUpdate = findUser(authenticatedUser.id()).getPassword();
    var request = new UpdateRequest(null, "jessica.jones@example.com", null, null);

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(authenticatedUser.registration().name()))
        .andExpect(jsonPath("$.email").value(request.email()))
        .andExpect(jsonPath("$.phone").value(authenticatedUser.registration().phone()));

    var persistedUser = findUser(authenticatedUser.id());
    assertThat(persistedUser.getName()).isEqualTo(authenticatedUser.registration().name());
    assertThat(persistedUser.getEmail()).isEqualTo(request.email());
    assertThat(persistedUser.getPhone()).isEqualTo(authenticatedUser.registration().phone());
    assertThat(persistedUser.getPassword()).isEqualTo(passwordBeforeUpdate);
  }

  @Test
  void itUpdatesOnlyTheUserPassword() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var passwordBeforeUpdate = findUser(authenticatedUser.id()).getPassword();
    var request = new UpdateRequest(null, null, "N3wStrong@123", null);

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(authenticatedUser.registration().name()))
        .andExpect(jsonPath("$.email").value(authenticatedUser.registration().email()))
        .andExpect(jsonPath("$.phone").value(authenticatedUser.registration().phone()));

    var persistedUser = findUser(authenticatedUser.id());
    assertThat(persistedUser.getName()).isEqualTo(authenticatedUser.registration().name());
    assertThat(persistedUser.getEmail()).isEqualTo(authenticatedUser.registration().email());
    assertThat(persistedUser.getPhone()).isEqualTo(authenticatedUser.registration().phone());
    assertThat(persistedUser.getPassword())
        .isNotEqualTo(passwordBeforeUpdate)
        .isNotEqualTo(request.password())
        .startsWith("$2");
  }

  @Test
  void itUpdatesOnlyTheUserPhone() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var passwordBeforeUpdate = findUser(authenticatedUser.id()).getPassword();
    var request = new UpdateRequest(null, null, null, "21988887777");

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(authenticatedUser.registration().name()))
        .andExpect(jsonPath("$.email").value(authenticatedUser.registration().email()))
        .andExpect(jsonPath("$.phone").value(request.phone()));

    var persistedUser = findUser(authenticatedUser.id());
    assertThat(persistedUser.getName()).isEqualTo(authenticatedUser.registration().name());
    assertThat(persistedUser.getEmail()).isEqualTo(authenticatedUser.registration().email());
    assertThat(persistedUser.getPhone()).isEqualTo(request.phone());
    assertThat(persistedUser.getPassword()).isEqualTo(passwordBeforeUpdate);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidNameProvider")
  void itValidatesTheNameWhenTheProvidedValueIsInvalid(
      String scenario, String invalidName, String expectedMessage) throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var request = new UpdateRequest(invalidName, null, null, null);

    assertValidationError(authenticatedUser, request, "name", expectedMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidEmailProvider")
  void itValidatesTheEmailWhenTheProvidedValueIsInvalid(
      String scenario, String invalidEmail, String expectedMessage) throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var request = new UpdateRequest(null, invalidEmail, null, null);

    assertValidationError(authenticatedUser, request, "email", expectedMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPasswordProvider")
  void itValidatesThePasswordWhenTheProvidedValueIsInvalid(
      String scenario, String invalidPassword, String expectedMessage) throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var request = new UpdateRequest(null, null, invalidPassword, null);

    assertValidationError(authenticatedUser, request, "password", expectedMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPhoneProvider")
  void itValidatesThePhoneWhenTheProvidedValueIsInvalid(
      String scenario, String invalidPhone, String expectedMessage) throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var request = new UpdateRequest(null, null, null, invalidPhone);

    assertValidationError(authenticatedUser, request, "phone", expectedMessage);
  }

  @Test
  void itRejectsAnUpdateWithoutAuthentication() throws Exception {
    var userId = apiTestSupport.createUserAndReturnId();
    var request = new UpdateRequest("Jessica Jones", null, null, null);

    patchJson(endpointFor(userId), request).andExpect(status().isUnauthorized());
  }

  @Test
  void itRejectsAnUpdateWithAnInvalidToken() throws Exception {
    var userId = apiTestSupport.createUserAndReturnId();
    var request = new UpdateRequest("Jessica Jones", null, null, null);

    patchJson(endpointFor(userId), request, "not-a-valid-jwt").andExpect(status().isUnauthorized());
  }

  @Test
  void itRejectsAnUpdateToAnotherUsersProfile() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var anotherUser =
        RegisterUserRequestBuilder.start()
            .withEmail("another.user@example.com")
            .withPhone("21999999999")
            .build();
    var anotherUserId = apiTestSupport.createUserAndReturnId(anotherUser);
    var request = new UpdateRequest("Unauthorized Change", null, null, null);

    patchJson(endpointFor(anotherUserId), request, authenticatedUser.accessToken())
        .andExpect(status().isForbidden());

    assertThat(findUser(anotherUserId).getName()).isEqualTo(anotherUser.name());
  }

  @Test
  void itRejectsAnEmailOwnedByAnotherUser() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var anotherUser =
        RegisterUserRequestBuilder.start()
            .withEmail("another.user@example.com")
            .withPhone("21999999999")
            .build();
    apiTestSupport.createUserAndReturnId(anotherUser);
    var request = new UpdateRequest(null, anotherUser.email(), null, null);

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("resource_already_exists"))
        .andExpect(jsonPath("$.field").value("email"));
  }

  @Test
  void itRejectsAPhoneOwnedByAnotherUser() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var anotherUser =
        RegisterUserRequestBuilder.start()
            .withEmail("another.user@example.com")
            .withPhone("21999999999")
            .build();
    apiTestSupport.createUserAndReturnId(anotherUser);
    var request = new UpdateRequest(null, null, null, anotherUser.phone());

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("resource_already_exists"))
        .andExpect(jsonPath("$.field").value("phone"));
  }

  @Test
  void itRejectsAnUpdateWhenNoChangesAreProvided() throws Exception {
    var authenticatedUser = registerAndAuthenticateUser();
    var request = new UpdateRequest(null, null, null, null);

    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.status").value(422))
        .andExpect(jsonPath("$.error").value("resource_not_changed"))
        .andExpect(jsonPath("$.field").value("id"));
  }

  private AuthenticatedUser registerAndAuthenticateUser() throws Exception {
    var registration = RegisterUserRequestBuilder.start().build();
    var userId = apiTestSupport.createUserAndReturnId(registration);
    var accessToken = apiTestSupport.authenticateAndReturnToken(registration);
    return new AuthenticatedUser(registration, userId, accessToken);
  }

  private void assertValidationError(
      AuthenticatedUser authenticatedUser,
      UpdateRequest request,
      String expectedField,
      String expectedMessage)
      throws Exception {
    patchJson(endpointFor(authenticatedUser.id()), request, authenticatedUser.accessToken())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value(expectedField))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  private UserJpa findUser(String userId) {
    return repository.findById(UUID.fromString(userId)).orElseThrow();
  }

  private String endpointFor(String userId) {
    return UserApiTestSupport.ENDPOINT + "/" + userId;
  }

  private static Stream<Arguments> invalidNameProvider() {
    return Stream.of(
        Arguments.of("blank", "", "The field 'name' cannot be null, empty or blank."),
        Arguments.of(
            "below minimum length", "Jo", "Name value must be between 3 and 150 characters long."),
        Arguments.of(
            "above maximum length",
            "a".repeat(151),
            "Name value must be between 3 and 150 characters long."),
        Arguments.of(
            "invalid characters",
            "John123",
            "Name must contain only letters, spaces, apostrophes, dots, and hyphens."));
  }

  private static Stream<Arguments> invalidEmailProvider() {
    return Stream.of(
        Arguments.of("blank email", "", "The field 'email' cannot be null, empty or blank."),
        Arguments.of("email without at sign", "invalid-email", "Invalid e-mail address."),
        Arguments.of("email without local part", "@example.com", "Invalid e-mail address."),
        Arguments.of("email without domain", "john@", "Invalid e-mail address."),
        Arguments.of(
            "email containing whitespace", "john doe@example.com", "Invalid e-mail address."));
  }

  private static Stream<Arguments> invalidPasswordProvider() {
    return Stream.of(
        Arguments.of("blank password", "", "The field 'password' cannot be null, empty or blank."),
        Arguments.of("password shorter than 8 characters", "Short1!", invalidPasswordMessage()),
        Arguments.of(
            "password missing uppercase letter", "alllowercase1!", invalidPasswordMessage()),
        Arguments.of(
            "password missing lowercase letter", "ALLUPPERCASE1!", invalidPasswordMessage()),
        Arguments.of("password missing digit", "NoDigits!!", invalidPasswordMessage()),
        Arguments.of(
            "password missing special character", "NoSpecial123", invalidPasswordMessage()),
        Arguments.of("password containing whitespace", "Has Space1!", invalidPasswordMessage()));
  }

  private static Stream<Arguments> invalidPhoneProvider() {
    return Stream.of(
        Arguments.of("blank", "", "The field 'phone' cannot be null, empty or blank."),
        Arguments.of("too short", "119999999", "Phone must contain 10 or 11 digits."),
        Arguments.of("too long", "119999999999", "Phone must contain 10 or 11 digits."),
        Arguments.of(
            "letters",
            "11999999999abc",
            "Phone must contain only numbers and formatting characters."));
  }

  private static String invalidPasswordMessage() {
    return "Password must be at least 8 characters and include uppercase, lowercase, digit, "
        + "and special character.";
  }

  private record AuthenticatedUser(RegisterRequest registration, String id, String accessToken) {}
}
