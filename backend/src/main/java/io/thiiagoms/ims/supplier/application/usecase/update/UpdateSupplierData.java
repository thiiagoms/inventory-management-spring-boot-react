package io.thiiagoms.ims.supplier.application.usecase.update;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import java.util.Optional;

public record UpdateSupplierData(
    Id id, Optional<SocialName> socialName, Optional<Address> address) {}
