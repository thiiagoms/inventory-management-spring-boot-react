package io.thiiagoms.ims.user.domain.repository;

import io.thiiagoms.ims.shared.domain.valueobject.Id;
import io.thiiagoms.ims.user.domain.User;
import io.thiiagoms.ims.user.domain.valueobject.Email;
import io.thiiagoms.ims.user.domain.valueobject.Phone;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findById(Id id);

  Optional<User> findByEmail(Email email);

  Optional<User> findByPhone(Phone phone);

  void save(User user);

  void destroy(Id id);
}
