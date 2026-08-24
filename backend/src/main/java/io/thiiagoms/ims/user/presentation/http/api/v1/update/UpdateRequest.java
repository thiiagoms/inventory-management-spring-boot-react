package io.thiiagoms.ims.user.presentation.http.api.v1.update;

import io.swagger.v3.oas.annotations.media.Schema;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.application.usecase.update.UpdateUserData;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import java.util.Optional;

public record UpdateRequest(
    @Schema(example = "Jane Doe") String name,
    @Schema(example = "jane.doe@gmail.com") String email,
    @Schema(example = "NewStrong@123") String password,
    @Schema(example = "(11) 98888-7777") String phone) {

  public UpdateUserData toCommand(String userId) {
    return new UpdateUserData(
        new Id(userId),
        Optional.ofNullable(name).map(Name::new),
        Optional.ofNullable(email).map(Email::new),
        Optional.ofNullable(password).map(PasswordPlain::new),
        Optional.ofNullable(phone).map(Phone::new));
  }
}
