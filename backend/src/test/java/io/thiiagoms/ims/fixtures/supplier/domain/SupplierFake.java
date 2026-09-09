package io.thiiagoms.ims.fixtures.supplier.domain;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;

public final class SupplierFake {
  private Id id = new Id("f1147c86-f31d-4683-9b86-46a665fed044");
  private SocialName socialName = new SocialName("Acme Supplies Ltda");
  private Cnpj cnpj = new Cnpj("11222333000181");
  private Address address = new Address("Praça da Sé, São Paulo - SP, 01001-000");
  private Timestamp createdAt = new Timestamp("2026-01-01T12:00:00Z");

  public static SupplierFake start() {
    return new SupplierFake();
  }

  public SupplierFake withId(Id id) {
    this.id = id;
    return this;
  }

  public Supplier build() {
    return Supplier.rehydrate(id, socialName, cnpj, address, createdAt);
  }
}
