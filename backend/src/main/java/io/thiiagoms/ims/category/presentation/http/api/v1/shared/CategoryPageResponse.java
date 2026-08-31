package io.thiiagoms.ims.category.presentation.http.api.v1.shared;

import io.thiiagoms.ims.category.application.dto.CategoryPageOutput;
import java.util.List;

public record CategoryPageResponse(
    List<CategoryResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public CategoryPageResponse {
    content = List.copyOf(content);
  }

  public static CategoryPageResponse from(CategoryPageOutput output) {
    return new CategoryPageResponse(
        output.content().stream().map(CategoryResponse::from).toList(),
        output.page(),
        output.size(),
        output.totalElements(),
        output.totalPages(),
        output.first(),
        output.last());
  }
}
