package io.thiiagoms.ims.supplier.infrastructure.config;

import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.time.Clock;
import io.thiiagoms.ims.supplier.application.service.SupplierFinder;
import io.thiiagoms.ims.supplier.application.service.SupplierUniqueness;
import io.thiiagoms.ims.supplier.application.usecase.destroy.DestroySupplier;
import io.thiiagoms.ims.supplier.application.usecase.register.RegisterSupplier;
import io.thiiagoms.ims.supplier.application.usecase.retrieve.RetrieveSupplier;
import io.thiiagoms.ims.supplier.application.usecase.retrieve.RetrieveSuppliers;
import io.thiiagoms.ims.supplier.application.usecase.update.UpdateSupplier;
import io.thiiagoms.ims.supplier.domain.repository.SupplierRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplierConfiguration {
  @Bean
  SupplierFinder supplierFinder(SupplierRepository repository) {
    return new SupplierFinder(repository);
  }

  @Bean
  SupplierUniqueness supplierUniqueness(SupplierRepository repository) {
    return new SupplierUniqueness(repository);
  }

  @Bean
  RegisterSupplier registerSupplier(
      SupplierRepository repository,
      IdentityGenerator identityGenerator,
      Clock clock,
      SupplierUniqueness uniqueness) {
    return new RegisterSupplier(repository, identityGenerator, clock, uniqueness);
  }

  @Bean
  RetrieveSupplier retrieveSupplier(SupplierFinder finder) {
    return new RetrieveSupplier(finder);
  }

  @Bean
  RetrieveSuppliers retrieveSuppliers(SupplierRepository repository) {
    return new RetrieveSuppliers(repository);
  }

  @Bean
  UpdateSupplier updateSupplier(
      SupplierFinder finder, SupplierRepository repository, SupplierUniqueness uniqueness) {
    return new UpdateSupplier(finder, repository, uniqueness);
  }

  @Bean
  DestroySupplier destroySupplier(SupplierFinder finder, SupplierRepository repository) {
    return new DestroySupplier(finder, repository);
  }
}
