package io.thiiagoms.ims.user.presentation.http.api.v1.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.thiiagoms.ims.user.application.usecase.auth.authenticate.AuthenticateData;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import jakarta.validation.constraints.NotBlank;

public record AuthenticateRequest(
    @NotBlank @Schema(example = "john.doe@gmail.com") String email,
    @NotBlank @Schema(example = "Strong@123") String password) {
  public AuthenticateData toCommand() {
    return new AuthenticateData(new Email(email), new PasswordPlain(password));
  }
}
