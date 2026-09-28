package com.cyedbooks.estructuras.hashtable.impl;

import com.cyedbooks.estructuras.hashtable.HashFunction;

public class MultiplicationHash<K> implements HashFunction<K> {

    private static final double A = 0.6180339887498949;

    @Override
    public int hash(K key, int tableSize) {
        if (tableSize <= 0) {
            throw new IllegalArgumentException("tableSize debe ser > 0");
        }
        long unsignedHash = key.hashCode() & 0xffffffffL;
        double fractionalPart = (unsignedHash * A) % 1.0;
        int index = (int) Math.floor(tableSize * fractionalPart);
        return Math.floorMod(index, tableSize);
    }
}
