package io.thiiagoms.ims.supplier.application.usecase.register;

import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;

public record RegisterSupplierData(SocialName socialName, Cnpj cnpj, Address address) {}
