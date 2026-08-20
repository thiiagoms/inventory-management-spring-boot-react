package io.thiiagoms.ims.user.domain;

import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordHash;
import io.thiiagoms.ims.user.domain.valueobject.Phone;

public class User {

  private final Id id;

  private Name name;

  private Email email;

  private PasswordHash password;

  private Phone phone;

  private final Role role;

  private User(Id id, Name name, Email email, Phone phone, PasswordHash password, Role role) {
    Guard.againstNull(Id.FIELD, id);
    Guard.againstNull(Name.FIELD, name);
    Guard.againstNull(Email.FIELD, email);
    Guard.againstNull(Phone.FIELD, phone);
    Guard.againstNull(PasswordHash.FIELD, password);
    Guard.againstNull(Role.FIELD, role);
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.role = role;
  }

  public static User register(Id id, Name name, Email email, Phone phone, PasswordHash password) {
    return new User(id, name, email, phone, password, Role.MANAGER);
  }

  public static User rehydrate(
      Id id, Name name, Email email, Phone phone, PasswordHash password, Role role) {
    return new User(id, name, email, phone, password, role);
  }

  public Id id() {
    return id;
  }

  public Name name() {
    return name;
  }

  public Email email() {
    return email;
  }

  public Phone phone() {
    return phone;
  }

  public PasswordHash password() {
    return password;
  }

  public Role role() {
    return role;
  }

  public void changeNameTo(Name name) {
    Guard.againstNull(Name.FIELD, name);

    if (this.name.equals(name)) {
      return;
    }

    this.name = name;
  }

  public void changeEmailTo(Email email) {
    Guard.againstNull(Email.FIELD, email);

    if (this.email.equals(email)) {
      return;
    }

    this.email = email;
  }

  public void changePhoneTo(Phone phone) {
    Guard.againstNull(Phone.FIELD, phone);

    if (this.phone.equals(phone)) {
      return;
    }

    this.phone = phone;
  }

  public void changePasswordTo(PasswordHash password) {
    Guard.againstNull(PasswordHash.FIELD, password);

    if (this.password.equals(password)) {
      return;
    }

    this.password = password;
  }
}
