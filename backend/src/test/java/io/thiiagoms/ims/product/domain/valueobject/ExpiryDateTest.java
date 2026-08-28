package io.thiiagoms.ims.product.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class ExpiryDateTest {
  private static final Clock CLOCK =
      Clock.fixed(Instant.parse("2026-08-28T12:00:00Z"), ZoneOffset.UTC);

  @Test
  void itAcceptsAFutureExpiryDate() {
    LocalDateTime value = LocalDateTime.of(2026, 8, 29, 12, 0);

    assertEquals(value, ExpiryDate.future(value, CLOCK).value());
  }

  @Test
  void itRejectsAnExpiryDateThatIsNotInTheFuture() {
    InvalidDomainArgumentException exception =
        assertThrows(
            InvalidDomainArgumentException.class,
            () -> ExpiryDate.future(LocalDateTime.of(2026, 8, 28, 12, 0), CLOCK));

    assertEquals(ExpiryDate.FIELD, exception.getField());
    assertEquals("Expiry date must be in the future.", exception.getMessage());
  }

  @Test
  void itCanRehydrateAnAlreadyExpiredProduct() {
    LocalDateTime value = LocalDateTime.of(2020, 1, 1, 0, 0);

    assertEquals(value, ExpiryDate.rehydrate(value).value());
  }
}
