package io.thiiagoms.ims.product.application.usecase.retrieve;

import io.thiiagoms.ims.product.application.dto.ProductPageOutput;
import io.thiiagoms.ims.product.domain.repository.ProductRepository;
import io.thiiagoms.ims.shared.domain.pagination.Pagination;

public class RetrieveProducts {

  private final ProductRepository repository;

  public RetrieveProducts(ProductRepository repository) {
    this.repository = repository;
  }

  public ProductPageOutput execute(Pagination pagination) {
    return ProductPageOutput.from(repository.findAll(pagination));
  }
}
