package io.thiiagoms.ims.product.infrastructure.config;

import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.application.usecase.register.RegisterProduct;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

  @Bean
  ProductUniqueness productUniqueness(ProductRepository repository) {
    return new ProductUniqueness(repository);
  }

  @Bean
  RegisterProduct registerProduct(
      ProductRepository repository, IdentityGenerator generator, ProductUniqueness uniqueness) {
    return new RegisterProduct(repository, generator, uniqueness);
  }
}
