package io.thiiagoms.ims.supplier.application.service;

import io.thiiagoms.ims.supplier.application.exception.SupplierCnpjAlreadyExistsException;
import io.thiiagoms.ims.supplier.application.exception.SupplierSocialNameAlreadyExistsException;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import io.thiiagoms.ims.supplier.domain.valueobject.Cnpj;
import io.thiiagoms.ims.supplier.domain.valueobject.SocialName;

public class SupplierUniqueness {

  private final SupplierRepository repository;

  public SupplierUniqueness(SupplierRepository repository) {
    this.repository = repository;
  }

  public void ensureSocialNameIsAvailable(SocialName name) {
    if (repository.findBySocialName(name).isPresent()) {
      throw SupplierSocialNameAlreadyExistsException.create();
    }
  }

  public void ensureCnpjIsAvailable(Cnpj cnpj) {
    if (repository.findByCnpj(cnpj).isPresent()) {
      throw SupplierCnpjAlreadyExistsException.create();
    }
  }
}
