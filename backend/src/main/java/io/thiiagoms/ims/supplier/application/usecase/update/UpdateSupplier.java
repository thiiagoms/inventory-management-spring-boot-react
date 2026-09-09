package io.thiiagoms.ims.supplier.application.usecase.update;

import io.thiiagoms.ims.supplier.application.dto.SupplierOutput;
import io.thiiagoms.ims.supplier.application.exception.SupplierNotChangedException;
import io.thiiagoms.ims.supplier.application.service.SupplierFinder;
import io.thiiagoms.ims.supplier.application.service.SupplierUniqueness;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import org.springframework.transaction.annotation.Transactional;

public class UpdateSupplier {

  private final SupplierFinder finder;

  private final SupplierUniqueness uniqueness;

  private final SupplierRepository repository;

  public UpdateSupplier(
      SupplierFinder finder, SupplierRepository repository, SupplierUniqueness uniqueness) {
    this.finder = finder;
    this.repository = repository;
    this.uniqueness = uniqueness;
  }

  @Transactional
  public SupplierOutput execute(UpdateSupplierData data) {
    var supplier = finder.byId(data.id());
    boolean changed = updateSocialName(supplier, data) | updateAddress(supplier, data);
    if (!changed) {
      throw SupplierNotChangedException.create();
    }
    repository.save(supplier);
    return SupplierOutput.from(supplier);
  }

  private boolean updateSocialName(Supplier supplier, UpdateSupplierData data) {
    return data.socialName()
        .filter(value -> !value.equals(supplier.socialName()))
        .map(
            value -> {
              uniqueness.ensureSocialNameIsAvailable(value);
              supplier.changeSocialNameTo(value);
              return true;
            })
        .orElse(false);
  }

  private boolean updateAddress(Supplier supplier, UpdateSupplierData data) {
    return data.address()
        .filter(value -> !value.equals(supplier.address()))
        .map(
            value -> {
              supplier.changeAddressTo(value);
              return true;
            })
        .orElse(false);
  }
}
