package io.thiiagoms.ims.user.presentation.http.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.thiiagoms.ims.fixtures.shared.presentation.http.ApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.UserApiTestSupport;
import io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register.RegisterUserRequestBuilder;
import io.thiiagoms.ims.user.domain.Role;
import io.thiiagoms.ims.user.infrastructure.persistence.repository.UserJpaRepository;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class RegisterApiTest extends ApiTestSupport {

  private final UserApiTestSupport apiTestSupport;

  private final UserJpaRepository repository;

  @Autowired
  public RegisterApiTest(MockMvc mockMvc, UserJpaRepository repository) {
    super(mockMvc);
    this.repository = repository;
    this.apiTestSupport = new UserApiTestSupport(this);
  }

  @Test
  void itCreateUserAndReturnCreatedata() throws Exception {
    var request = RegisterUserRequestBuilder.start().build();

    String response =
        postJson(UserApiTestSupport.ENDPOINT, request)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.name()))
            .andExpect(jsonPath("$.email").value(request.email()))
            .andExpect(jsonPath("$.phone").value(request.phone()))
            .andReturn()
            .getResponse()
            .getContentAsString();

    var userId = readId(response);
    var User = repository.findById(UUID.fromString(userId));

    assertThat(User).isPresent();
    assertThat(User)
        .hasValueSatisfying(
            persistedUser -> {
              assertThat(persistedUser.getName()).isEqualTo(request.name());
              assertThat(persistedUser.getEmail()).isEqualTo(request.email());
              assertThat(persistedUser.getPhone()).isEqualTo(request.phone());
              assertThat(persistedUser.getPassword())
                  .isNotEqualTo(request.password())
                  .startsWith("$2");
              assertThat(persistedUser.getRole()).isEqualTo(Role.MANAGER.name());
            });
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidNameProvider")
  void itShouldValidateNameWhenProvidedValueIsInvalid(
      String scenario, String invalidName, String expectedMessage) throws Exception {
    var request = RegisterUserRequestBuilder.start().withName(invalidName).build();

    postJson(UserApiTestSupport.ENDPOINT, request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("name"))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidEmailProvider")
  void itShouldValidateEmailWhenProvidedValueIsInvalid(
      String scenario, String invalidEmail, String expectedMessage) throws Exception {
    var request = RegisterUserRequestBuilder.start().withEmail(invalidEmail).build();

    postJson(UserApiTestSupport.ENDPOINT, request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("email"))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPasswordProvider")
  void itShouldValidatePasswordWhenProvidedValueIsInvalid(
      String scenario, String invalidPassword, String expectedMessage) throws Exception {
    var request = RegisterUserRequestBuilder.start().withPassword(invalidPassword).build();

    postJson(UserApiTestSupport.ENDPOINT, request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("password"))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPhoneProvider")
  void itShouldValidatePhoneWhenProvidedValueIsInvalid(
      String scenario, String invalidPhone, String expectedMessage) throws Exception {
    var request = RegisterUserRequestBuilder.start().withPhone(invalidPhone).build();

    postJson(UserApiTestSupport.ENDPOINT, request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("validation_failed"))
        .andExpect(jsonPath("$.field").value("phone"))
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  @Test
  void itShouldRejectDuplicatedEmail() throws Exception {
    var existingUser = RegisterUserRequestBuilder.start().withEmail("john.doe@gmail.com").build();

    var duplicatedEmailRequest =
        RegisterUserRequestBuilder.start().withEmail("john.doe@gmail.com").build();

    apiTestSupport.createUserAndReturnId(existingUser);

    postJson(UserApiTestSupport.ENDPOINT, duplicatedEmailRequest)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("resource_already_exists"))
        .andExpect(jsonPath("$.field").value("email"))
        .andExpect(jsonPath("$.message").value("A user with this e-mail already exists."));
  }

  @Test
  void itShouldRejectDuplicatedPhone() throws Exception {
    var existingAccount = RegisterUserRequestBuilder.start().build();
    var duplicatedPhoneRequest =
        RegisterUserRequestBuilder.start().withEmail("another@example.com").build();

    apiTestSupport.createUserAndReturnId(existingAccount);

    postJson(UserApiTestSupport.ENDPOINT, duplicatedPhoneRequest)
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("resource_already_exists"))
        .andExpect(jsonPath("$.field").value("phone"))
        .andExpect(jsonPath("$.message").value("A user with this phone already exists."));
  }

  private static Stream<Arguments> invalidNameProvider() {
    return Stream.of(
        Arguments.of("blank", "", "must not be blank"),
        Arguments.of(
            "below minimum length", "Jo", "Name value must be between 3 and 150 characters long."),
        Arguments.of(
            "above maximum length",
            "a".repeat(151),
            "Name value must be between 3 and 150 characters long."),
        Arguments.of(
            "invalid characters",
            "John123",
            "Name must contain only letters, spaces, " + "apostrophes, dots, and hyphens."));
  }

  private static Stream<Arguments> invalidEmailProvider() {
    return Stream.of(
        Arguments.of("blank email", "", "must not be blank"),
        Arguments.of("email without at sign", "invalid-email", "Invalid e-mail address."),
        Arguments.of("email without local part", "@example.com", "Invalid e-mail address."),
        Arguments.of("email without domain", "john@", "Invalid e-mail address."),
        Arguments.of(
            "email containing whitespace", "john doe@example.com", "Invalid e-mail address."));
  }

  private static Stream<Arguments> invalidPasswordProvider() {
    return Stream.of(
        Arguments.of("blank password", "", "must not be blank"),
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
        Arguments.of("blank", "", "must not be blank"),
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
}
