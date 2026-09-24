package com.cyedbooks.estructuras.hashtable;

import com.cyedbooks.estructuras.core.interfaces.Collection;
import com.cyedbooks.estructuras.model.Entry;

/**
 * Contrato de una estructura clave-valor con acceso amortizado O(1),
 * implementado por {@code HashTable}.
 * <p>
 * Extiende {@code Collection<Entry<K, V>>}: el "elemento" que cuenta para
 * {@code size()}/{@code isEmpty()}/{@code clear()} es cada par clave-valor
 * almacenado. También extiende {@code Iterable<Entry<K, V>>} para permitir
 * recorrer todas las entradas con un {@code for-each}.
 *
 * @param <K> tipo de las claves
 * @param <V> tipo de los valores
 */
public interface HashTableInterface<K, V> extends Collection<Entry<K, V>>, Iterable<Entry<K, V>> {

    /**
     * Inserta o actualiza el valor asociado a {@code key}.
     *
     * @return el valor previamente asociado a {@code key}, o {@code null}
     *         si la clave no existía
     */
    V put(K key, V value);

    /**
     * @return el valor asociado a {@code key}, o {@code null} si no existe
     */
    V get(K key);

    /**
     * Remueve la entrada asociada a {@code key}, si existe.
     *
     * @return el valor removido, o {@code null} si la clave no existía
     */
    V remove(K key);

    /**
     * @return {@code true} si existe una entrada para {@code key}
     */
    boolean containsKey(K key);

    /**
     * @return {@code true} si algún valor almacenado es igual a {@code value}
     */
    boolean containsValue(V value);

    /**
     * @return todas las claves almacenadas, en orden no especificado
     */
    Iterable<K> keys();

    /**
     * @return todos los valores almacenados, en orden no especificado (puede
     *         contener duplicados si varias claves comparten valor)
     */
    Iterable<V> values();

    /**
     * @return todas las entradas clave-valor almacenadas, en orden no especificado
     */
    Iterable<Entry<K, V>> entries();

    /**
     * @return el factor de carga actual: {@code size() / capacidadInterna}
     */
    double loadFactor();
}
