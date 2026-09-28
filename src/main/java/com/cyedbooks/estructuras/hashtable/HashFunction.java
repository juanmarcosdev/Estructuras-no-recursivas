package com.cyedbooks.estructuras.hashtable;

@FunctionalInterface
public interface HashFunction<K> {

    int hash(K key, int tableSize);
}
