package com.daromi.stash.core.cache;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public final class LruCache<K, V> implements Cache<K, V> {

  private final ConcurrentMap<K, V> store;

  private LruCache() {
    this.store = new ConcurrentHashMap<>();
  }

  @Override
  public Optional<V> get(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    final var value = store.get(key);

    return Optional.ofNullable(value);
  }

  @Override
  public V getIfPresent(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    return store.get(key);
  }

  @Override
  public V getOrElse(final K key, final Supplier<? extends V> supplier) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(supplier, "supplier must not be null");

    return store.containsKey(key) ? store.get(key) : supplier.get();
  }

  @Override
  public void put(final K key, final V value) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(value, "value must not be null");

    store.put(key, value);
  }

  @Override
  public void invalidate(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    store.remove(key);
  }
}
