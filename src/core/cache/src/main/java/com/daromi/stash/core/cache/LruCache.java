package com.daromi.stash.core.cache;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;

public final class LruCache<K, V> implements Cache<K, V> {

  private final int maximumSize;

  private final ConcurrentHashMap<K, V> store;

  private final ConcurrentLinkedDeque<K> priority;

  public LruCache(final int maximumSize) {
    if (maximumSize <= 0) {
      throw new IllegalArgumentException("maximum size must be positive");
    }

    this.maximumSize = maximumSize;

    this.store = new ConcurrentHashMap<>();
    this.priority = new ConcurrentLinkedDeque<>();
  }

  @Override
  public Optional<V> get(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    if (!store.containsKey(key)) {
      return Optional.empty();
    }

    updatePriority(key);

    final var value = store.get(key);

    return Optional.of(value);
  }

  @Override
  public V getIfPresent(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    if (!store.containsKey(key)) {
      return null;
    }

    updatePriority(key);

    return store.get(key);
  }

  @Override
  public V getOrElse(final K key, final Supplier<V> supplier) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(supplier, "supplier must not be null");

    if (!store.containsKey(key)) {
      return supplier.get();
    }

    updatePriority(key);

    return store.get(key);
  }

  @Override
  public void put(final K key, final V value) {
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(value, "value must not be null");

    synchronized (this) {
      if (store.size() == maximumSize) {
        final var first = priority.removeFirst();
        store.remove(first);
      }

      updatePriority(key);

      store.put(key, value);
    }

    assert store.size() <= maximumSize : "cache size exceeded maximum";
  }

  @Override
  public void invalidate(final K key) {
    Objects.requireNonNull(key, "key must not be null");

    if (!store.containsKey(key)) {
      return;
    }

    synchronized (this) {
      store.remove(key);
      priority.remove(key);
    }
  }

  private void updatePriority(final K key) {
    synchronized (this) {
      priority.remove(key);
      priority.add(key);
    }

    assert priority.size() <= maximumSize : "cache size exceeded maximum";
  }
}
