package com.daromi.stash.core.cache;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public final class LruCache<K, V> implements Cache<K, V> {

  private final int capacity;

  private final ConcurrentHashMap<K, V> store;

  private final ConcurrentLinkedQueue<K> priority;

  @Nullable private final EvictionListener<K, V> listener;

  public LruCache(final int capacity, @Nullable final EvictionListener<K, V> listener) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be positive");
    }

    this.capacity = capacity;
    this.listener = listener;
    this.store = new ConcurrentHashMap<>();
    this.priority = new ConcurrentLinkedQueue<>();
  }

  public LruCache(final int capacity) {
    this(capacity, null);
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
      if (store.size() == capacity) {
        evict();
      }

      updatePriority(key);

      store.put(key, value);
    }

    assert store.size() <= capacity : "cache size exceeded capacity";
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

    assert priority.size() <= capacity : "cache size exceeded capacity";
  }

  private void evict() {
    K key;
    V value;

    synchronized (this) {
      key = priority.remove();
      value = store.remove(key);
    }

    if (listener != null) {
      listener.evicted(key, value);
    }
  }
}
