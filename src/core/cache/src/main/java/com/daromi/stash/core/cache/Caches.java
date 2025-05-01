package com.daromi.stash.core.cache;

import java.util.Objects;

public final class Caches {

  private Caches() {}

  public static <K, V> Cache<K, V> lru(final int capacity) {
    return new LruCache<>(capacity);
  }

  public static <K, V> Cache<K, V> lru(final int capacity, final EvictionListener<K, V> listener) {
    Objects.requireNonNull(listener, "listener must not be null");

    return new LruCache<>(capacity, listener);
  }
}
