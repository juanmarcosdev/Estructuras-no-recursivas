package com.cyedbooks.estructuras.model;

import java.util.Objects;

/**
 * Par clave-valor almacenado por {@code HashTable}. Incluye un enlace
 * {@code next} propio para poder actuar, sin envolturas adicionales, como
 * nodo de una lista de colisiones cuando la tabla usa la estrategia de
 * encadenamiento separado ({@code ChainingStrategy}); las estrategias de
 * direccionamiento abierto simplemente ignoran ese enlace.
 *
 * @param <K> tipo de la clave
 * @param <V> tipo del valor
 */
public class Entry<K, V> {

    /** Marca especial usada por las estrategias de direccionamiento abierto
     *  para representar un slot que tuvo una entrada y fue eliminada
     *  ("tombstone"), de forma que las secuencias de sondeo sigan siendo
     *  válidas tras una remoción. */
    public static final Object DELETED_MARKER = new Object();

    private final K key;
    private V value;
    private Entry<K, V> next;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Entry<K, V> getNext() {
        return next;
    }

    public void setNext(Entry<K, V> next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entry<?, ?> other)) {
            return false;
        }
        return Objects.equals(key, other.key) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
