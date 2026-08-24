package io.thiiagoms.ims.user.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JwtPropertiesTest {

  private static final String VALID_SECRET =
      "VGhpcy1pcy1hLXRlc3Qtb25seS1qd3Qtc2VjcmV0LXdpdGgtYXQtbGVhc3QtMzItYnl0ZXM=";

  @Test
  void itRejectsABlankSecret() {
    assertThrows(IllegalArgumentException.class, () -> new JwtProperties(" ", 60));
  }

  @Test
  void itRejectsASecretThatIsNotBase64Encoded() {
    assertThrows(IllegalArgumentException.class, () -> new JwtProperties("not-base64!", 60));
  }

  @Test
  void itRejectsASecretShorterThan256Bits() {
    assertThrows(IllegalArgumentException.class, () -> new JwtProperties("c2hvcnQ=", 60));
  }

  @Test
  void itRejectsANonPositiveTtl() {
    assertThrows(IllegalArgumentException.class, () -> new JwtProperties(VALID_SECRET, 0));
  }
}
