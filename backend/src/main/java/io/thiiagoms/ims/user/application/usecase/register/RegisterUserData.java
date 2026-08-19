package io.thiiagoms.ims.user.application.usecase.register;

import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Phone;

public record RegisterUserData(
        Name name,
        Email email,
        PasswordPlain password,
        Phone phone) {
}
