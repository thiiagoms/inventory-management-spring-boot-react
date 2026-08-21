package io.thiiagoms.ims.user.application.usecase.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.fixtures.user.infrastructure.persistence.repository.UserMemoryRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.exception.EmailAlreadyExistsException;
import io.thiiagoms.ims.user.application.exception.PhoneAlreadyExistsException;
import io.thiiagoms.ims.user.application.exception.UserNotChangedException;
import io.thiiagoms.ims.user.application.exception.UserNotFoundException;
import io.thiiagoms.ims.user.application.service.UserFinder;
import io.thiiagoms.ims.user.application.service.UserUniqueness;
import io.thiiagoms.ims.user.domain.repository.UserRepository;
import io.thiiagoms.ims.user.domain.security.PasswordEncoder;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {

  private Id id;

  private UserRepository repository;

  @Mock private PasswordEncoder passwordEncoder;

  private UpdateUser useCase;

  @BeforeEach
  void setUp() {
    id = new Id("3780baf2-deed-448d-a763-ce7b06efd394");
    repository = new UserMemoryRepository();

    useCase =
        new UpdateUser(
            new UserFinder(repository),
            passwordEncoder,
            repository,
            new UserUniqueness(repository));
  }

  @Test
  void itUpdatesTheEntireUser() {
    var name = new Name("Mary Jane Watson");
    var email = new Email("mary.jane@example.com");
    var phone = new Phone("21988887777");
    var password = new PasswordPlain("N3wP4ssword!");

    var expectedGeneratedPasswordHash =
        new PasswordHash("$2y$12$YAZ54ki7cF3Oa/Em6/9WW.MwaAl65WbjV6nZl63qR9SqCmNBH5RT.");

    var searchableUser = UserFake.start().withId(id).build();
    repository.save(searchableUser);

    var command =
        new UpdateUserData(
            id, Optional.of(name), Optional.of(email), Optional.of(password), Optional.of(phone));

    when(passwordEncoder.matches(command.password().get(), searchableUser.password()))
        .thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn(expectedGeneratedPasswordHash);

    var user = useCase.execute(command);

    assertEquals(id.value(), user.id());
    assertEquals(command.name().get().value(), user.name());
    assertEquals(command.email().get().value(), user.email());
    assertEquals(command.phone().get().value(), user.phone());
  }

  @Test
  void itUpdatesOnlyTheUserName() {
    var name = new Name("Mary Jane Watson");

    var searchableUser = UserFake.start().withId(id).build();
    repository.save(searchableUser);

    var command =
        new UpdateUserData(
            id, Optional.of(name), Optional.empty(), Optional.empty(), Optional.empty());

    var user = useCase.execute(command);

    assertEquals(id.value(), user.id());
    assertEquals(command.name().get().value(), user.name());
    assertEquals(searchableUser.email().value(), user.email());

    verify(passwordEncoder, never()).matches(any(), any());
    verify(passwordEncoder, never()).encode(any());
  }

  @Test
  void itUpdatesOnlyTheUserEmail() {
    var email = new Email("mary.jane@example.com");

    var searchableUser = UserFake.start().withId(id).build();
    repository.save(searchableUser);

    var command =
        new UpdateUserData(
            id, Optional.empty(), Optional.of(email), Optional.empty(), Optional.empty());

    var user = useCase.execute(command);

    assertEquals(id.value(), user.id());
    assertEquals(searchableUser.name().value(), user.name());
    assertEquals(command.email().get().value(), user.email());
  }

  @Test
  void itUpdatesOnlyTheUserPassword() {
    var password = new PasswordPlain("N3wP4ssword!");
    var expectedGeneratedPasswordHash =
        new PasswordHash("$2y$12$YAZ54ki7cF3Oa/Em6/9WW.MwaAl65WbjV6nZl63qR9SqCmNBH5RT.");

    var searchableUser = UserFake.start().withId(id).build();
    repository.save(searchableUser);

    var command =
        new UpdateUserData(
            id, Optional.empty(), Optional.empty(), Optional.of(password), Optional.empty());

    when(passwordEncoder.matches(command.password().get(), searchableUser.password()))
        .thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn(expectedGeneratedPasswordHash);

    var user = useCase.execute(command);

    assertEquals(id.value(), user.id());
    assertEquals(searchableUser.name().value(), user.name());
    assertEquals(searchableUser.email().value(), user.email());
  }

  @Test
  void itRejectsAnUpdateWhenTheUserDoesNotExist() {
    var command =
        new UpdateUserData(
            id, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> useCase.execute(command));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("User not found with the provided id.", exception.getMessage());

    verify(passwordEncoder, never()).matches(any(), any());
    verify(passwordEncoder, never()).encode(any());
  }

  @Test
  void itRejectsAnUpdateWhenTheEmailIsAlreadyOwnedByAnotherUser() {
    var email = new Email("mary.jane@example.com");

    repository.save(
        UserFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withEmail(email)
            .build());
    repository.save(UserFake.start().withId(id).build());

    var command =
        new UpdateUserData(
            id, Optional.empty(), Optional.of(email), Optional.empty(), Optional.empty());

    EmailAlreadyExistsException exception =
        assertThrows(EmailAlreadyExistsException.class, () -> useCase.execute(command));

    assertEquals(Email.FIELD, exception.getField());
    assertEquals("A user with this e-mail already exists.", exception.getMessage());

    verify(passwordEncoder, never()).matches(any(), any());
    verify(passwordEncoder, never()).encode(any());
  }

  @Test
  void itRejectsAnUpdateWhenThePhoneIsAlreadyOwnedByAnotherUser() {
    Phone phone = new Phone("21988887777");
    repository.save(
        UserFake.start()
            .withId(new Id("baa86496-638f-4beb-bc03-de2f7589ad63"))
            .withPhone(phone)
            .build());
    repository.save(UserFake.start().withId(id).build());

    UpdateUserData command =
        new UpdateUserData(
            id, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(phone));

    PhoneAlreadyExistsException exception =
        assertThrows(PhoneAlreadyExistsException.class, () -> useCase.execute(command));

    assertEquals(Phone.FIELD, exception.getField());
    assertEquals("A user with this phone already exists.", exception.getMessage());
    verify(passwordEncoder, never()).matches(any(), any());
    verify(passwordEncoder, never()).encode(any());
  }

  @Test
  void itRejectsAnUpdateWhenNothingChangesInTheUser() {

    var name = new Name("Mary Jane Watson");
    var email = new Email("mary.jane@example.com");
    var phone = new Phone("21988887777");
    var password = new PasswordPlain("P4SsW0rd!)");

    var searchableUser =
        UserFake.start()
            .withId(id)
            .withName(name)
            .withEmail(email)
            .withPhone(phone)
            .withPassword(
                new PasswordHash("$2y$12$8jyTh39c6xV2J7fshilp7.bQ2dIT8RoWBp6o0F54e1guD8wcnux1G"))
            .build();
    repository.save(searchableUser);

    var command =
        new UpdateUserData(
            id, Optional.of(name), Optional.of(email), Optional.of(password), Optional.of(phone));

    when(passwordEncoder.matches(command.password().get(), searchableUser.password()))
        .thenReturn(true);

    UserNotChangedException exception =
        assertThrows(UserNotChangedException.class, () -> useCase.execute(command));

    assertEquals(Id.FIELD, exception.getField());
    assertEquals("No changes were detected for the user.", exception.getMessage());

    verify(passwordEncoder, never()).encode(any());
  }
}
