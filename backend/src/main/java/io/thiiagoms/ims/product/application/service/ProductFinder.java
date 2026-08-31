package io.thiiagoms.ims.product.application.service;

import io.thiiagoms.ims.product.application.exception.ProductNotFoundException;
import io.thiiagoms.ims.product.domain.Product;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.shared.application.exception.NotFoundException;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.Optional;
import java.util.function.Supplier;

public class ProductFinder {

  private final ProductRepository repository;

  public ProductFinder(ProductRepository repository) {
    this.repository = repository;
  }

  public Product byId(Id id) {
    return findOrFail(
        () -> repository.findById(id),
        () -> ProductNotFoundException.with("Product not found with the provided id.", Id.FIELD));
  }

  private <T> T findOrFail(Supplier<Optional<T>> resolver, Supplier<NotFoundException> exception) {
    return resolver.get().orElseThrow(exception);
  }
}
