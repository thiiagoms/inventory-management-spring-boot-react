package io.thiiagoms.ims.shared.domain.time;

import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;

public interface Clock {

  Timestamp now();
}
