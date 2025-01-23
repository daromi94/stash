package com.daromi.stash.core.cache;

@FunctionalInterface
public interface EvictionListener<K, V> {

  void evicted(final K key, final V value);
}
