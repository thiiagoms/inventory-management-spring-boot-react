package io.thiiagoms.ims.shared.domain.pagination;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;

public record Pagination(int page, int size) {
  public static final int DEFAULT_PAGE = 0;
  public static final int DEFAULT_SIZE = 20;
  public static final int MAX_SIZE = 100;

  public Pagination {
    if (page < 0) {
      throw InvalidDomainArgumentException.with("Page must be zero or greater.", "page");
    }
    if (size < 1 || size > MAX_SIZE) {
      throw InvalidDomainArgumentException.with(
          "Size must be between 1 and %d.".formatted(MAX_SIZE), "size");
    }
  }
}
