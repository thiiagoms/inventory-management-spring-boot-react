package io.thiiagoms.ims.supplier.application.usecase.register;

import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.time.Clock;
import io.thiiagoms.ims.supplier.application.dto.SupplierOutput;
import io.thiiagoms.ims.supplier.application.service.SupplierUniqueness;
import io.thiiagoms.ims.supplier.domain.Supplier;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import org.springframework.transaction.annotation.Transactional;

public class RegisterSupplier {
  private final SupplierRepository repository;
  private final IdentityGenerator identityGenerator;
  private final Clock clock;
  private final SupplierUniqueness uniqueness;

  public RegisterSupplier(
      SupplierRepository repository,
      IdentityGenerator identityGenerator,
      Clock clock,
      SupplierUniqueness uniqueness) {
    this.repository = repository;
    this.identityGenerator = identityGenerator;
    this.clock = clock;
    this.uniqueness = uniqueness;
  }

  @Transactional
  public SupplierOutput execute(RegisterSupplierData data) {
    uniqueness.ensureCnpjIsAvailable(data.cnpj());
    var supplier =
        Supplier.register(
            identityGenerator.generate(),
            data.socialName(),
            data.cnpj(),
            data.address(),
            clock.now());
    repository.save(supplier);
    return SupplierOutput.from(supplier);
  }
}
