package io.thiiagoms.ims.fixtures.user.presentation.http.api.v1.register;

import io.thiiagoms.ims.user.presentation.http.api.v1.register.RegisterUserRequest;

public class RegisterUserRequestBuilder {

  private String name;

  private String email;

  private String password;

  private String phone;

  private RegisterUserRequestBuilder() {
    name = "Matt Murdock";
    email = "ilovedaredevil@gmail.com";
    password = "P4sSw0rd!_)(";
    phone = "11999999999";
  }

  public static RegisterUserRequestBuilder start() {
    return new RegisterUserRequestBuilder();
  }

  public RegisterUserRequestBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public RegisterUserRequestBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public RegisterUserRequestBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public RegisterUserRequestBuilder withPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public RegisterUserRequest build() {
    return new RegisterUserRequest(name, email, password, phone);
  }
}
