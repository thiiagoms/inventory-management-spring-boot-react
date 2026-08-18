package io.thiiagoms.ims.shared.domain.identity;

import io.thiiagoms.ims.shared.domain.valueobject.Id;

public interface IdentityGenerator {
    Id generate();
}
