package io.thiiagoms.ims.supplier.presentation.http.api.v1;

import io.thiiagoms.ims.supplier.application.usecase.register.RegisterSupplierData;
import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import jakarta.validation.constraints.NotBlank;

public record RegisterSupplierRequest(
    @NotBlank String socialName, @NotBlank String cnpj, @NotBlank String address) {
  public RegisterSupplierData toCommand() {
    return new RegisterSupplierData(
        new SocialName(socialName), new Cnpj(cnpj), new Address(address));
  }
}
