package io.thiiagoms.ims.supplier.infrastructure.persistence.mapper;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;
import io.thiiagoms.ims.supplier.infrastructure.persistence.model.SupplierJpa;
import java.time.Instant;
import java.util.UUID;

public final class SupplierMapper {
  private SupplierMapper() {}

  public static SupplierJpa toPersistence(Supplier supplier) {
    return SupplierJpa.builder()
        .id(UUID.fromString(supplier.id().value()))
        .socialName(supplier.socialName().value())
        .cnpj(supplier.cnpj().value())
        .address(supplier.address().value())
        .createdAt(Instant.parse(supplier.createdAt().value()))
        .build();
  }

  public static Supplier toDomain(SupplierJpa supplier) {
    return Supplier.rehydrate(
        new Id(supplier.getId().toString()),
        new SocialName(supplier.getSocialName()),
        new Cnpj(supplier.getCnpj()),
        new Address(supplier.getAddress()),
        new Timestamp(supplier.getCreatedAt().toString()));
  }
}
