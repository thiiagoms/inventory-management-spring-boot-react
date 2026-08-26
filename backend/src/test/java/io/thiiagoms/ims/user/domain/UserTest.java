package io.thiiagoms.ims.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.thiiagoms.ims.fixtures.user.domain.UserFake;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  void itRegistersAnUserWithDefaultValues() {
    var id = new Id("d8807a2c-f98d-478d-82ee-69965f490ec4");
    var name = new Name("John Doe");
    var email = new Email("ilovelaravel@gmail.com");
    var phone = new Phone("11999999999");
    var password = new PasswordHash("$2y$12$iF6w435uv0afaojVWrI4Lu8mc0FU.Oyp9BlTQcqCIDcnXbd0rz4sS");

    User user = User.register(id, name, email, phone, password);

    assertEquals(id, user.id());
    assertEquals(name, user.name());
    assertEquals(email, user.email());
    assertEquals(phone, user.phone());
    assertEquals(password, user.password());
    assertEquals(Role.MANAGER, user.role());
    assertTrue(user.lastLoginAt().isEmpty());
  }

  @Test
  void itRehydratesAnUserWithGivenValues() {
    var id = new Id("d8807a2c-f98d-478d-82ee-69965f490ec4");
    var name = new Name("John Doe");
    var email = new Email("ilovelaravel@gmail.com");
    var phone = new Phone("11999999999");
    var password = new PasswordHash("$2y$12$iF6w435uv0afaojVWrI4Lu8mc0FU.Oyp9BlTQcqCIDcnXbd0rz4sS");
    var lastLoginAt = new Timestamp("2026-08-21T12:00:00Z");

    User user =
        User.rehydrate(id, name, email, phone, password, Role.ADMIN, Optional.of(lastLoginAt));

    assertEquals(id, user.id());
    assertEquals(name, user.name());
    assertEquals(email, user.email());
    assertEquals(phone, user.phone());
    assertEquals(password, user.password());
    assertEquals(Role.ADMIN, user.role());
    assertEquals(lastLoginAt, user.lastLoginAt().orElseThrow());
  }

  @Test
  void itRecordsTheLastSuccessfulLogin() {
    var user = UserFake.start().build();
    var authenticatedAt = new Timestamp("2026-08-21T12:00:00Z");

    user.recordLoginAt(authenticatedAt);

    assertEquals(authenticatedAt, user.lastLoginAt().orElseThrow());
  }

  @Test
  void itChangesTheUserName() {
    User user = UserFake.start().build();
    var newName = new Name("Peter Parker");
    user.changeNameTo(newName);
    assertEquals(newName, user.name());
  }

  @Test
  void itChangesTheUserEmail() {
    User user = UserFake.start().build();
    var newEmail = new Email("ilovespiderman@gmail.com");
    user.changeEmailTo(newEmail);
    assertEquals(newEmail, user.email());
  }

  @Test
  void itChangesTheUserPassword() {
    User user = UserFake.start().build();
    var newPassword =
        new PasswordHash("$2y$12$YAZ54ki7cF3Oa/Em6/9WW.MwaAl65WbjV6nZl63qR9SqCmNBH5RT.");
    user.changePasswordTo(newPassword);
    assertEquals(newPassword, user.password());
  }

  @Test
  void itChangesTheUserPhone() {
    User user = UserFake.start().build();
    Phone newPhone = new Phone("21988887777");
    user.changePhoneTo(newPhone);
    assertEquals(newPhone, user.phone());
  }

  @Test
  void itDoesNotChangeTheUserNameWhenItIsUnchanged() {
    var name = new Name("John Doe");
    var user = UserFake.start().withName(name).build();

    user.changeNameTo(name);
    assertEquals(name, user.name());
  }

  @Test
  void itDoesNotChangeTheUserEmailWhenItIsUnchanged() {
    var email = new Email("ilovejava@gmail.com");

    var user = UserFake.start().withEmail(email).build();

    user.changeEmailTo(email);
    assertEquals(email, user.email());
  }

  @Test
  void itDoesNotChangeTheUserPhoneWhenItIsUnchanged() {
    var currentPhone = new Phone("11999999999");
    var equivalentPhone = new Phone("(11) 99999-9999");
    var user = UserFake.start().withPhone(currentPhone).build();

    user.changePhoneTo(equivalentPhone);

    assertSame(currentPhone, user.phone());
  }

  @Test
  void itDoesNotChangeTheUserPasswordWhenItIsUnchanged() {
    var password = new PasswordHash("$2y$12$YAZ54ki7cF3Oa/Em6/9WW.MwaAl65WbjV6nZl63qR9SqCmNBH5RT.");
    var user = UserFake.start().withPassword(password).build();

    user.changePasswordTo(password);
    assertEquals(password, user.password());
  }
}
