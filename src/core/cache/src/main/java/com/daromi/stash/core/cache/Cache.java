package com.daromi.stash.core.cache;

import java.util.Optional;
import java.util.function.Supplier;

public interface Cache<K, V> {

  Optional<V> get(final K key);

  V getIfPresent(final K key);

  V getOrElse(final K key, final Supplier<V> supplier);

  void put(final K key, final V value);

  void invalidate(final K key);
}
