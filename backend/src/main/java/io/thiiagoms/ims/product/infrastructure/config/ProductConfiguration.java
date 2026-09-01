package io.thiiagoms.ims.product.infrastructure.config;

import io.thiiagoms.ims.category.application.service.CategoryFinder;
import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.application.usecase.destroy.DestroyProduct;
import io.thiiagoms.ims.product.application.usecase.register.RegisterProduct;
import io.thiiagoms.ims.product.application.usecase.retrieve.RetrieveProduct;
import io.thiiagoms.ims.product.application.usecase.retrieve.RetrieveProducts;
import io.thiiagoms.ims.product.application.usecase.update.UpdateProduct;
import io.thiiagoms.ims.product.domain.SkuGenerator;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

  @Bean
  ProductFinder productFinder(ProductRepository repository) {
    return new ProductFinder(repository);
  }

  @Bean
  ProductUniqueness productUniqueness(ProductRepository repository) {
    return new ProductUniqueness(repository);
  }

  @Bean
  RegisterProduct registerProduct(
      CategoryFinder categoryFinder,
      ProductRepository repository,
      IdentityGenerator generator,
      ProductUniqueness uniqueness) {
    SkuGenerator skuGenerator = Sku::generate;
    return new RegisterProduct(categoryFinder, repository, generator, skuGenerator, uniqueness);
  }

  @Bean
  RetrieveProduct retrieveProduct(ProductFinder finder) {
    return new RetrieveProduct(finder);
  }

  @Bean
  RetrieveProducts retrieveProducts(ProductRepository repository) {
    return new RetrieveProducts(repository);
  }

  @Bean
  UpdateProduct updateProduct(
      ProductFinder finder, ProductRepository repository, ProductUniqueness uniqueness) {
    return new UpdateProduct(finder, repository, uniqueness);
  }

  @Bean
  DestroyProduct destroyProduct(ProductFinder finder, ProductRepository repository) {
    return new DestroyProduct(finder, repository);
  }
}
