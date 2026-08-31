package io.thiiagoms.ims.category.application.dto;

import io.thiiagoms.ims.category.domain.Category;
import io.thiiagoms.ims.shared.domain.pagination.Page;
import java.util.List;

public record CategoryPageOutput(
    List<CategoryOutput> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public CategoryPageOutput {
    content = List.copyOf(content);
  }

  public static CategoryPageOutput from(Page<Category> categories) {
    return new CategoryPageOutput(
        categories.content().stream().map(CategoryOutput::from).toList(),
        categories.page(),
        categories.size(),
        categories.totalElements(),
        categories.totalPages(),
        categories.first(),
        categories.last());
  }
}
