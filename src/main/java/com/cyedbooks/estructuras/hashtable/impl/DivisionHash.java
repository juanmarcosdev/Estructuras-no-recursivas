package com.cyedbooks.estructuras.hashtable.impl;

import com.cyedbooks.estructuras.hashtable.HashFunction;

/**
 * Método de la división: {@code h(k) = k mod m}.
 * <p>
 * Se usa {@code key.hashCode()} como la representación entera {@code k} de
 * la clave (válido para cualquier tipo {@code K}), y se enmascara el signo
 * con {@code & 0x7fffffff} antes de aplicar el módulo para evitar índices
 * negativos (un {@code hashCode()} en Java puede ser negativo, y el
 * operador {@code %} de Java preserva el signo del dividendo).
 * <p>
 * Es simple y rápida, pero su calidad depende de que {@code m} (la
 * capacidad de la tabla) no comparta factores comunes con los patrones de
 * las claves; se recomienda un {@code m} primo cuando sea posible.
 *
 * @param <K> tipo de la clave
 */
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
