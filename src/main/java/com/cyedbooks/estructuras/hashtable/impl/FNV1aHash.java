package com.cyedbooks.estructuras.hashtable.impl;

import com.cyedbooks.estructuras.hashtable.HashFunction;

/**
 * Implementación del algoritmo <a href="http://www.isthe.com/chongo/tech/comp/fnv/">FNV-1a</a>
 * (Fowler–Noll–Vo, variante "a") de 32 bits, especializada para claves de
 * tipo {@link String}. Procesa la cadena carácter a carácter con un bucle
 * iterativo, produciendo una distribución de buena calidad para texto —
 * habitualmente mejor que usar directamente {@code String.hashCode()} para
 * cadenas con prefijos comunes.
 */
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
