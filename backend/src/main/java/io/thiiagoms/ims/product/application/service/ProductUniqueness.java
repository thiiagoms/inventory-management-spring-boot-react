package io.thiiagoms.ims.product.application.service;

import io.thiiagoms.ims.product.application.exception.ProductSkuAlreadyExistsException;
import io.thiiagoms.ims.product.application.exception.ProductTitleAlreadyExistsException;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.product.domain.valueobject.Sku;
import io.thiiagoms.ims.product.domain.valueobject.Title;

public class ProductUniqueness {
  private final ProductRepository repository;

  public ProductUniqueness(ProductRepository repository) {
    this.repository = repository;
  }

  public void ensureTitleIsAvailable(Title title) {
    if (repository.findByTitle(title).isPresent()) {
      throw ProductTitleAlreadyExistsException.create();
    }
  }

  public void ensureSkuIsAvailable(Sku sku) {
    if (repository.findBySku(sku).isPresent()) {
      throw ProductSkuAlreadyExistsException.create();
    }
  }
}
