package com.cyedbooks.estructuras.hashtable.impl;

import com.cyedbooks.estructuras.hashtable.HashFunction;

public class DivisionHash<K> implements HashFunction<K> {

    @Override
    public int hash(K key, int tableSize) {
        if (tableSize <= 0) {
            throw new IllegalArgumentException("tableSize debe ser > 0");
        }
        int h = key.hashCode();
        return (h & 0x7fffffff) % tableSize;
    }
}
