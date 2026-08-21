package io.thiiagoms.ims.user.application.usecase.update;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Name;
import io.thiiagoms.ims.user.domain.valueobject.PasswordPlain;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import java.util.Optional;

public record UpdateUserData(
    Id id,
    Optional<Name> name,
    Optional<Email> email,
    Optional<PasswordPlain> password,
    Optional<Phone> phone) {}
