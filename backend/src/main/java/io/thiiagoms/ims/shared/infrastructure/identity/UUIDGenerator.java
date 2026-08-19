package io.thiiagoms.ims.shared.infrastructure.identity;

import io.thiiagoms.ims.shared.domain.identity.IdentityGenerator;
import io.thiiagoms.ims.shared.domain.valueobject.Id;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator implements IdentityGenerator {

  @Override
  public Id generate() {
    UUID uuid = generateRandomUUIDV4();
    return new Id(uuid.toString());
  }

  private UUID generateRandomUUIDV4() {
    return UUID.randomUUID();
  }
}
