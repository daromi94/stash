package com.daromi.stash.core.cache;

public interface Cache<K, V> {

  V get(final K key);

  void put(final K key, final V value);
}
