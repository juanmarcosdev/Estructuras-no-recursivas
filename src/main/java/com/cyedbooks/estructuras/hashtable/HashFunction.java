package com.cyedbooks.estructuras.hashtable;

/**
 * Estrategia para convertir una clave en un índice dentro de una tabla de
 * tamaño dado. Se pasa {@code tableSize} en cada llamada (en vez de fijarlo
 * en el constructor) para que la misma instancia de función hash pueda
 * reutilizarse a través de los distintos tamaños de tabla que resultan de
 * los redimensionamientos de {@code HashTable}.
 *
 * @param <K> tipo de la clave
 */
@FunctionalInterface
public interface HashFunction<K> {

    /**
     * @param key       clave a transformar (nunca {@code null})
     * @param tableSize capacidad actual de la tabla (debe ser {@code > 0})
     * @return un índice en el rango {@code [0, tableSize)}
     */
    int hash(K key, int tableSize);
}
