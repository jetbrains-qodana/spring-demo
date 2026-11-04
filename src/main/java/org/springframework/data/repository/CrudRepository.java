package org.springframework.data.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {
  <S extends T> S save(S entity);

  Optional<T> findById(ID id);
}
