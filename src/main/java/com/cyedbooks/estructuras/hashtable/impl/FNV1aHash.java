package com.cyedbooks.estructuras.hashtable.impl;

import com.cyedbooks.estructuras.hashtable.HashFunction;

public class FNV1aHash implements HashFunction<String> {

    private static final int FNV_OFFSET_BASIS = 0x811c9dc5;
    private static final int FNV_PRIME = 0x01000193;

    @Override
    public int hash(String key, int tableSize) {
        if (tableSize <= 0) {
            throw new IllegalArgumentException("tableSize debe ser > 0");
        }
        int hash = FNV_OFFSET_BASIS;
        for (int i = 0; i < key.length(); i++) {
            hash ^= key.charAt(i);
            hash *= FNV_PRIME;
        }
        return (hash & 0x7fffffff) % tableSize;
    }
}
