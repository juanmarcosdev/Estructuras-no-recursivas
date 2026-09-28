package com.cyedbooks.estructuras.hashtable;

import com.cyedbooks.estructuras.core.interfaces.Collection;
import com.cyedbooks.estructuras.model.Entry;

public interface HashTableInterface<K, V> extends Collection<Entry<K, V>>, Iterable<Entry<K, V>> {

    V put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    Iterable<K> keys();

    Iterable<V> values();

    Iterable<Entry<K, V>> entries();

    double loadFactor();
}
