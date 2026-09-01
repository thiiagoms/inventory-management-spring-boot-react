package io.thiiagoms.ims.product.application.usecase.destroy;

import io.thiiagoms.ims.product.application.service.ProductFinder;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import org.springframework.transaction.annotation.Transactional;

public class DestroyProduct {
  private final ProductFinder finder;
  private final ProductRepository repository;

  public DestroyProduct(ProductFinder finder, ProductRepository repository) {
    this.finder = finder;
    this.repository = repository;
  }

  @Transactional
  public void execute(Id id) {
    finder.byId(id);
    repository.destroy(id);
  }
}
