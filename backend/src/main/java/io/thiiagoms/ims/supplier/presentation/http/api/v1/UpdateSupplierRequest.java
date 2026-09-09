package io.thiiagoms.ims.supplier.presentation.http.api.v1;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.supplier.application.usecase.update.UpdateSupplierData;
import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import java.util.Optional;

public record UpdateSupplierRequest(String socialName, String address) {
  public UpdateSupplierData toCommand(String id) {
    return new UpdateSupplierData(
        new Id(id),
        Optional.ofNullable(socialName).map(SocialName::new),
        Optional.ofNullable(address).map(Address::new));
  }
}
