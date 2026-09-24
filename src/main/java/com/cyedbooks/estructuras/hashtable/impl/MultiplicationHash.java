package com.cyedbooks.estructuras.hashtable.impl;

import com.cyedbooks.estructuras.hashtable.HashFunction;

/**
 * Método de la multiplicación de Knuth:
 * {@code h(k) = floor(m * ((k * A) mod 1))}, con
 * {@code A = (sqrt(5) - 1) / 2 ≈ 0.6180339887} (la razón áurea), el valor
 * que Knuth recomienda por repartir mejor las colisiones
 * independientemente de {@code m}.
 * <p>
 * A diferencia del método de la división, no requiere que {@code m} sea
 * primo para lograr una buena distribución, por lo que combina bien con
 * capacidades que son potencia de dos (como las que usa {@code HashTable}
 * al redimensionarse).
 *
 * @param <K> tipo de la clave
 */
public class MultiplicationHash<K> implements HashFunction<K> {

    /** Constante A de Knuth (razón áurea - 1). */
    private static final double A = 0.6180339887498949;

    @Override
    public int hash(K key, int tableSize) {
        if (tableSize <= 0) {
            throw new IllegalArgumentException("tableSize debe ser > 0");
        }
        // Se trata el hashCode como un entero sin signo de 32 bits para
        // evitar que los valores negativos distorsionen la parte fraccionaria.
        long unsignedHash = key.hashCode() & 0xffffffffL;
        double fractionalPart = (unsignedHash * A) % 1.0;
        int index = (int) Math.floor(tableSize * fractionalPart);
        // Salvaguarda por errores de redondeo en punto flotante.
        return Math.floorMod(index, tableSize);
    }
}
