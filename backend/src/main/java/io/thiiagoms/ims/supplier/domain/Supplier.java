package io.thiiagoms.ims.supplier.domain;

import io.thiiagoms.ims.shared.domain.support.Guard;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import io.thiiagoms.ims.supplier.domain.valueobject.Address;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;

public final class Supplier {
  private final Id id;
  private SocialName socialName;
  private Cnpj cnpj;
  private Address address;
  private final Timestamp createdAt;

  private Supplier(Id id, SocialName socialName, Cnpj cnpj, Address address, Timestamp createdAt) {
    Guard.againstNull(Id.FIELD, id);
    Guard.againstNull(SocialName.FIELD, socialName);
    Guard.againstNull(Cnpj.FIELD, cnpj);
    Guard.againstNull(Address.FIELD, address);
    Guard.againstNull("createdAt", createdAt);
    this.id = id;
    this.socialName = socialName;
    this.cnpj = cnpj;
    this.address = address;
    this.createdAt = createdAt;
  }

  public static Supplier register(
      Id id, SocialName socialName, Cnpj cnpj, Address address, Timestamp createdAt) {
    return new Supplier(id, socialName, cnpj, address, createdAt);
  }

  public static Supplier rehydrate(
      Id id, SocialName socialName, Cnpj cnpj, Address address, Timestamp createdAt) {
    return new Supplier(id, socialName, cnpj, address, createdAt);
  }

  public Id id() {
    return id;
  }

  public SocialName socialName() {
    return socialName;
  }

  public Cnpj cnpj() {
    return cnpj;
  }

  public Address address() {
    return address;
  }

  public Timestamp createdAt() {
    return createdAt;
  }

  public void changeSocialNameTo(SocialName socialName) {
    Guard.againstNull(SocialName.FIELD, socialName);
    this.socialName = socialName;
  }

  public void changeCnpjTo(Cnpj cnpj) {
    Guard.againstNull(Cnpj.FIELD, cnpj);
    this.cnpj = cnpj;
  }

  public void changeAddressTo(Address address) {
    Guard.againstNull(Address.FIELD, address);
    this.address = address;
  }
}
