package io.thiiagoms.ims.fixtures.user.domain;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.domain.Role;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.Phone;

public class UserFake {

  private Id id;

  private Name name;

  private Email email;

  private PasswordHash password;

  private Phone phone;

  private Role role;

  private UserFake() {
    id = new Id("eaf16b7a-a0f9-4bb7-b57a-b91991c0710c");
    name = new Name("Matt Murdock");
    email = new Email("ilovejava@gmail.com");
    password = new PasswordHash("$2y$12$Mn6Kd4NcVsubXdHG8uIdDOIoLfpbqR/vHayPRLjPzNSGTUzpshrvi");
    phone = new Phone("11999999999");
    role = Role.MANAGER;
  }

  public static UserFake start() {
    return new UserFake();
  }

  public UserFake withId(Id id) {
    this.id = id;
    return this;
  }

  public UserFake withName(Name name) {
    this.name = name;
    return this;
  }

  public UserFake withEmail(Email email) {
    this.email = email;
    return this;
  }

  public UserFake withPhone(Phone phone) {
    this.phone = phone;
    return this;
  }

  public UserFake withPassword(PasswordHash password) {
    this.password = password;
    return this;
  }

  public UserFake withRole(Role role) {
    this.role = role;
    return this;
  }

  public User build() {
    return User.rehydrate(id, name, email, phone, password, role);
  }
}
