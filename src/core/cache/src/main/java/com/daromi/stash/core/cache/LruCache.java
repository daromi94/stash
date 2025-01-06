package com.daromi.stash.core.cache;

import java.util.Objects;
import java.util.Optional;

public final class LruCache<K, V> implements Cache<K, V> {

  private LruCache() {}

  @Override
  public Optional<V> get(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    // TODO

    return Optional.empty();
  }

  @Override
  public void put(final K key, final V value) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(value, "value must not be null");

    // TODO
  }
}
