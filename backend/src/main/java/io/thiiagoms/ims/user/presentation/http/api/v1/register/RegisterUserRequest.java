package io.thiiagoms.ims.user.presentation.http.api.v1.register;

import io.swagger.v3.oas.annotations.media.Schema;
import io.thiiagoms.ims.user.application.usecase.register.RegisterUserData;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
    @NotBlank @Schema(example = "John Doe") String name,
    @NotBlank @Schema(example = "john.doe@gmail.com") String email,
    @NotBlank @Schema(example = "Strong@123") String password,
    @NotBlank
        @Schema(
            description = "Brazilian phone with a two-digit area code. Formatting is optional.",
            example = "(11) 99999-9999")
        String phone) {
  public RegisterUserData toCommand() {
    return new RegisterUserData(
        new Name(name), new Email(email), new PasswordPlain(password), new Phone(phone));
  }
}
