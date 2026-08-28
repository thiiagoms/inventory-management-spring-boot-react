package io.thiiagoms.ims.product.application.usecase.register;

import io.thiiagoms.ims.product.application.dto.ProductOutput;
import io.thiiagoms.ims.product.application.service.ProductUniqueness;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import org.springframework.transaction.annotation.Transactional;

public class RegisterProduct {
  private final ProductRepository repository;
  private final IdentityGenerator identityGenerator;
  private final ProductUniqueness uniqueness;

  public RegisterProduct(
      ProductRepository repository,
      IdentityGenerator identityGenerator,
      ProductUniqueness uniqueness) {
    this.repository = repository;
    this.identityGenerator = identityGenerator;
    this.uniqueness = uniqueness;
  }

  @Transactional
  public ProductOutput execute(RegisterProductData data) {
    uniqueness.ensureTitleIsAvailable(data.title());
    uniqueness.ensureSkuIsAvailable(data.sku());
    Product product = build(data);

    repository.save(product);

    return ProductOutput.from(product);
  }

  private Product build(RegisterProductData data) {
    return Product.register(
        identityGenerator.generate(),
        data.title(),
        data.description(),
        data.sku(),
        data.imageUrl(),
        data.price(),
        data.stockQuantity(),
        data.categoryId(),
        data.expiryDate());
  }
}
