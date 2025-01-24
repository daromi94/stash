package com.daromi.stash.core.cache;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class TimeLimitCache<K, V> implements Cache<K, V> {

  private final ConcurrentHashMap<K, V> store;

  public TimeLimitCache() {
    this.store = new ConcurrentHashMap<>();
  }

  @Override
  public Optional<V> get(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    if (!store.containsKey(key)) {
      return Optional.empty();
    }

    final var value = store.get(key);

    return Optional.of(value);
  }

  @Override
  public V getIfPresent(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    if (!store.containsKey(key)) {
      return null;
    }

    return store.get(key);
  }

  @Override
  public V getOrElse(final K key, final Supplier<V> supplier) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(supplier, "supplier must not be null");

    if (!store.containsKey(key)) {
      return supplier.get();
    }

    return store.get(key);
  }

  @Override
  public void put(final K key, final V value) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(value, "value must not be null");

    // TODO
  }

  @Override
  public void invalidate(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    // TODO
  }
}
