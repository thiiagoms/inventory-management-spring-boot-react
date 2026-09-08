package io.thiiagoms.ims.supplier.presentation.http.api.v1;

import io.thiiagoms.ims.supplier.application.dto.SupplierOutput;

public record SupplierResponse(
    String id, String socialName, String cnpj, String address, String createdAt) {
  public static SupplierResponse from(SupplierOutput output) {
    return new SupplierResponse(
        output.id(), output.socialName(), output.cnpj(), output.address(), output.createdAt());
  }
}
