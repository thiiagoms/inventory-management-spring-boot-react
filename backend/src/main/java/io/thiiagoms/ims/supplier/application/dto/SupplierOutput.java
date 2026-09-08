package io.thiiagoms.ims.supplier.application.dto;

import io.thiiagoms.ims.supplier.domain.Supplier;

public record SupplierOutput(
    String id, String socialName, String cnpj, String address, String createdAt) {
  public static SupplierOutput from(Supplier supplier) {
    return new SupplierOutput(
        supplier.id().value(),
        supplier.socialName().value(),
        supplier.cnpj().value(),
        supplier.address().value(),
        supplier.createdAt().value());
  }
}
